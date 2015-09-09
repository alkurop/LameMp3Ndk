package com.omar.retromp3recorder.app.recorder;

import android.content.Context;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import com.omar.retromp3recorder.app.Constants;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.callbacks.IRecorderCallback;
import com.omar.retromp3recorder.app.utils.ContextHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by omar on 17.08.15.
 */
public class VoiceRecorder {
    private Context context;

    private IRecorderCallback callback;
    private static Handler callbackHandler;

    private AudioRecord recorder;
    private FileOutputStream output;

    private final int STATUS_NORMAL = 0;
    private final int STATUS_ERROR = 1;
    private final int STATUS_ID = 2;

    private int mSampleRate;
    private int mBitRate;

    private short channelConfig = Constants.CHANNEL_PRESETS[0];
    private int quality = Constants.QUALITY_PRESETS[1];
    private short audioFormat = Constants.AUDIO_FORMAT_PRESETS[1];


    private String filePath;

    private int minBufferSize;
    private short[] buffer;
    private byte[] mp3buffer;
    private boolean recording;

    private int sleepMillis = 100;
    private long counter;

    public VoiceRecorder(String _filepath, IRecorderCallback _callback) {
        context = ContextHelper.getContext();

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
        sendHandlerMessage(STATUS_NORMAL, context.getString(R.string.recorder_stopped));

    }


    private void initBuffer() {
        buffer = new short[mSampleRate * (16 / 8) * 5]; // SampleRate[Hz]
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

        private void recThread () {
            counterThread();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                    try {
                        File outFile = new File(filePath);
                        if (outFile.exists()) {
                            outFile.delete();
                        }
                        outFile.createNewFile();
                        VoiceRecorder.this.initBuffer();
                        VoiceRecorder.this.initOutput();
                        VoiceRecorder.this.initRecorder();
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

                        if (outFile.exists()) {
                            VoiceRecorder.this.sendHandlerMessage(STATUS_NORMAL, context.getString(R.string.file_saved_to) + filePath);
                            VoiceRecorder.this.sendHandlerMessage(STATUS_NORMAL, String.format(context.getString(R.string.audio_length) +
                                    "= %.1f" + context.getString(R.string.seconds), ((float) counter) / 1000));
                            VoiceRecorder.this.sendHandlerMessage(STATUS_NORMAL, String.format(context.getString(R.string.file_size) + " = %.1f " + Constants.KB, ((float) outFile.length()) / 1000));
                            VoiceRecorder.this.sendHandlerMessage(STATUS_NORMAL, String.format("%.1f " + Constants.KB + context.getString(R.string.compression_rate), ((float) outFile.length()) / ((float) counter)));

                        } else
                            VoiceRecorder.this.sendHandlerMessage(STATUS_ERROR, context.getString(R.string.error_saving_file_to) + filePath);


                    } catch (Exception e) {
                        recording = false;
                        VoiceRecorder.this.sendHandlerMessage(STATUS_ERROR, e.getMessage());
                    }

                }
            }).start();
        }

        private AudioRecord findAudioRecord ()throws Exception {
            minBufferSize = AudioRecord.getMinBufferSize(mSampleRate, channelConfig, audioFormat);
            if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {

                AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, mSampleRate, channelConfig, audioFormat, minBufferSize * 4);
                try {
                    LameModule.init(mSampleRate, 1, mSampleRate, mBitRate, quality);
                } catch (Exception e) {
                    throw new Exception(context.getString(R.string.error_init_recorder));
                }
                if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    String logMessage = String.format(context.getString(R.string.recording_mp3_at) + " %d " + Constants.KBPS + " , %d " + Constants.HZ, mBitRate, mSampleRate);
                    sendHandlerMessage(STATUS_NORMAL, logMessage);
                    return recorder;

                } else {
                    throw new Exception(context.getString(R.string.error_init_recorder));
                }

            } else {
                throw new Exception(context.getString(R.string.audioRecord_bad_value));
            }


        }

        private void sendHandlerMessage ( int status, String data){
            Message m = new Message();
            m.what = status;
            Bundle b = new Bundle();
            b.putString(Constants.DATA, data);
            m.setData(b);
            callbackHandler.sendMessage(m);

        }


        private void setupCallbackHandler () {
            callbackHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case STATUS_NORMAL:
                            callback.normalMessage(message.getData().getString(Constants.DATA));
                            break;
                        case STATUS_ERROR:
                            callback.onErrorOccured(message.getData().getString(Constants.DATA));
                            break;
                        case STATUS_ID:
                            callback.sendRecorderId(message.getData().getInt(Constants.DATA));
                            break;
                    }
                }
            };

        }


    }