package com.omar.lamemp3ndk.app.recorder;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.omar.lamemp3ndk.app.Constants;
import com.omar.lamemp3ndk.app.callbacks.IRecorderCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by omar on 17.08.15.
 */
public class VoiceRecorder {

    private AudioRecord recorder;
    private IRecorderCallback callback;
    private Handler callbackHandler;
    private String filePath;
    private FileOutputStream output;


    private short channelConfig = Constants.CHANNEL_PRESETS[0];
    private int quality = Constants.QUALITY_PRESETS[1];
    private int mSampleRate = Constants.SAMPLE_RATE_PRESETS[3];
    private short audioFormat = Constants.AUDIO_FORMAT_PRESETS[1];
    private int mBitRate = Constants.BIT_RATE_PRESETS[1];


    private int minBufferSize;
    private short[] buffer;
    private byte[] mp3buffer;
    private boolean recording;


    private final int STATUS_NORMAL = 0;
    private final int STATUS_ERROR = 1;
    private final int STATUS_ID = 2;

    private long counter;

    private final String DATA = "data";
    private final String TAG = "recorder";

    private int sleepMillis = 100;


    public VoiceRecorder(String _filepath, IRecorderCallback _callback) {
        filePath = _filepath;
        callback = _callback;
        setupCallbackHandler();
    }


    public void recordStart(int _mSampleRate, int _mBitRate) {
        mSampleRate = _mSampleRate;
        mBitRate = _mBitRate;
        minBufferSize = AudioRecord.getMinBufferSize(mSampleRate, channelConfig, audioFormat);
        recording = true;
        recThread();

    }

    public void recordStop() {
        recording = false;
        sendHandlerMessage(STATUS_NORMAL, "recorder stopped");

    }


    private void initBuffer() {
        buffer = new short[mSampleRate * (16 / 8) * 1 * 5]; // SampleRate[Hz]
        mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
    }

    private void initRecorder() throws Exception {
        recorder = findAudioRecord();
    }

    private void initOutput() throws FileNotFoundException {
        output = null;
        output = new FileOutputStream(new File(filePath));
    }

    private void counterThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                counter = 0;
                while (recording) {
                    try {
                        Thread.sleep(sleepMillis);
                        counter += sleepMillis;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    private void recThread() {
        counterThread();
        new Thread() {
            @Override
            public void run() {

                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

                try {
                    File outFile = new File(filePath);
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    outFile.createNewFile();
                    initBuffer();
                    initOutput();
                    initRecorder();
                    recorder.startRecording();
                    int readSize = 0;
                    while (recording) {

                        readSize = recorder.read(buffer, 0, minBufferSize);
                        int encResult = LameModule.encode(buffer, buffer, readSize, mp3buffer);
                        output.write(mp3buffer, 0, encResult);

                    }
                    int flushResult = LameModule.flush(mp3buffer);
                    if (flushResult != 0) {
                        output.write(mp3buffer, 0, flushResult);
                    }
                    output.close();
                    recorder.stop();
                    recorder.release();
                    LameModule.close();
                    File file = new File(filePath);
                    if (file.exists()) {
                        sendHandlerMessage(STATUS_NORMAL, "file saved to " + filePath);
                        sendHandlerMessage(STATUS_NORMAL, String.format("audio length = %.1f seconds", ((float) counter) / 1000));
                        sendHandlerMessage(STATUS_NORMAL, String.format("file size = %.1f kb", ((float) file.length()) / 1000));
                        sendHandlerMessage(STATUS_NORMAL, String.format(" %.1f kb per second", ((float) file.length()
                        ) / ((float) counter) ));

                    } else sendHandlerMessage(STATUS_ERROR, "error saving file to " + filePath);


                } catch (Exception e) {
                    recording = false;
                    sendHandlerMessage(STATUS_ERROR, e.getMessage());
                }
            }
        }.start();
    }

    private AudioRecord findAudioRecord() throws Exception {


        Log.d(TAG, "Attempting rate " + mSampleRate + "Hz, bits: " + audioFormat + ", channel: " + channelConfig);


        minBufferSize = AudioRecord.getMinBufferSize(mSampleRate, channelConfig, audioFormat);

        if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {

            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, mSampleRate, channelConfig, audioFormat, minBufferSize * 4);
            try {
                LameModule.init(mSampleRate, 1, mSampleRate, mBitRate, quality);
            } catch (Exception e) {
                throw new Exception("Error initializing recorder");
            }
            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {

                String logMessage = String.format("recording MP3 at %d kbps , %d Hz ", mBitRate, mSampleRate);
                sendHandlerMessage(STATUS_NORMAL, logMessage);

                return recorder;
            } else {
                throw new Exception("Error initializing recorder");
            }

        } else {
            throw new Exception("AudioRecord.ERROR_BAD_VALUE");
        }


    }

    private void sendHandlerMessage(int status, String data) {
        Message m = new Message();
        m.what = status;
        Bundle b = new Bundle();
        b.putString(DATA, data);
        m.setData(b);
        callbackHandler.sendMessage(m);

    }


    private void sendHandlerMessage(int status, int data) {
        Message m = new Message();
        m.what = status;
        Bundle b = new Bundle();
        b.putInt(DATA, data);
        m.setData(b);
        callbackHandler.sendMessage(m);

    }

    private void setupCallbackHandler() {
        callbackHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case STATUS_NORMAL:
                        callback.NormalMessage(message.getData().getString(DATA));
                        break;
                    case STATUS_ERROR:
                        callback.OnErrorOccured(message.getData().getString(DATA));
                        break;
                    case STATUS_ID:
                        callback.SendRecorderId(message.getData().getInt(DATA));
                        break;
                }
            }
        };

    }


}