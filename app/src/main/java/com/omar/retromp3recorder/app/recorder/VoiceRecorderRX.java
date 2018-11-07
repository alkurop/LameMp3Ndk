package com.omar.retromp3recorder.app.recorder;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.support.v4.util.Pair;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.stringer.Stringer;
import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@NotUnitTestable
public final class VoiceRecorderRX implements VoiceRecorder {

    private static final short channelConfig = CHANNEL_PRESETS[0];
    private static final int quality = QUALITY_PRESETS[1];
    private static final short audioFormat = AUDIO_FORMAT_PRESETS[1];
    private final Stringer stringer;
    private final Scheduler scheduler;
    private final Subject<Event> events = PublishSubject.create();
    private final AtomicLong elapsed = new AtomicLong(0);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    VoiceRecorderRX(Stringer stringer, Scheduler scheduler) {
        this.stringer = stringer;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<Event> observeEvents() {
        return events;
    }

    @Override
    public void record(RecorderProps props) {
        int minBufferSize = AudioRecord.getMinBufferSize(
                props.sampleRate.value,
                channelConfig,
                audioFormat
        );

        Single<Pair<File, AudioRecord>> recorderParams = Single
                .zip(
                        createOutputFile(props.filepath),
                        createRecorder(
                                minBufferSize,
                                props.sampleRate.value,
                                props.bitRate.value
                        ),
                        Pair::new
                );

        Completable recorderCompletable = recorderParams
                .flatMapCompletable(fileAudioRecordPair -> record(
                        fileAudioRecordPair.first,
                        fileAudioRecordPair.second,
                        props.sampleRate.value
                ));

        Disposable disposable = recorderCompletable
                .onErrorResumeNext(throwable -> {
                    events.onNext(new Error(throwable.getMessage()));
                    throwable.printStackTrace();
                    return Completable.complete();
                })
                .subscribeOn(scheduler)
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @Override
    public boolean isRecording() {
        return compositeDisposable.size() > 0;
    }

    @Override
    public void stopRecord() {
        compositeDisposable.clear();
    }

    private short[] createBuffer(int sampleRate) {
        return new short[sampleRate * (16 / 8) * 5];
    }

    private byte[] createMp3Buffer(short[] buffer) {
        return new byte[(int) (7200 + buffer.length * 2 * 1.25)];
    }

    private Single<AudioRecord> createRecorder(
            int minBufferSize,
            int sampleRate,
            int bitRate
    ) {
        return Single.fromCallable(() -> findAudioRecord(
                minBufferSize,
                sampleRate, bitRate
        ));
    }

    private Single<File> createOutputFile(String filePath) {
        return Single.fromCallable(() -> {
                    File outFile = new File(filePath);
                    if (outFile.exists()) {
                        @SuppressWarnings("unused") boolean delete = outFile.delete();
                    }
                    boolean fileWasCreated = outFile.createNewFile();
                    if (!fileWasCreated) {
                        throw new IOException(stringer.getString(
                                R.string.file_was_not_created,
                                filePath
                        ));
                    }
                    return outFile;
                }
        );
    }

    private Completable record(File outFile, AudioRecord recorder, int sampleRate) {
        return Completable
                .create(emitter -> {
                    try {
                        elapsed.set(System.currentTimeMillis());
                        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                        FileOutputStream output = new FileOutputStream(outFile);
                        emitter.setCancellable(() -> {
                            recorder.stop();
                            recorder.release();
                            LameModule.close();
                            output.close();
                            sendFinishLog(outFile);

                        });

                        short[] buffer = createBuffer(sampleRate);
                        byte[] mp3Buffer = createMp3Buffer(buffer);
                        int minBufferSize = AudioRecord.getMinBufferSize(
                                sampleRate,
                                channelConfig,
                                audioFormat
                        );
                        recorder.startRecording();

                        int readSize;
                        while (!emitter.isDisposed()) {
                            readSize = recorder.read(
                                    buffer,
                                    0,
                                    minBufferSize
                            );
                            int encResult = LameModule.encode(
                                    buffer,
                                    buffer,
                                    readSize,
                                    mp3Buffer
                            );
                            output.write(mp3Buffer, 0, encResult);
                        }
                        int flushResult = LameModule.flush(mp3Buffer);
                        if (flushResult != 0) {
                            output.write(mp3Buffer, 0, flushResult);
                        }
                    } catch (Throwable e) {
                        emitter.tryOnError(e);
                    }
                });
    }

    private void sendFinishLog(File outFile) {
        long counter = System.currentTimeMillis() - elapsed.get();
        String absolutePath = outFile.getAbsolutePath();
        if (outFile.exists()) {
            String[] messages = new String[]{
                    stringer.getString(
                            R.string.file_saved_to,
                            absolutePath
                    ),
                    stringer.getString(
                            R.string.audio_length,
                            ((float) counter) / 1000
                    ),
                    stringer.getString(
                            R.string.file_size,
                            ((float) outFile.length()) / 1000
                    ),
                    stringer.getString(
                            R.string.compression_rate,
                            (float) (outFile.length()) / counter
                    )
            };

            for (String message : messages) {
                events.onNext(new Message(message));

            }
        } else
            events.onNext(new Error(stringer.getString(
                    R.string.error_saving_file_to,
                    absolutePath
            )));
    }


    private AudioRecord findAudioRecord(
            int minBufferSize,
            int sampleRate,
            int bitRate
    ) throws Exception {
        if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
            AudioRecord recorder = new AudioRecord(
                    MediaRecorder.AudioSource.DEFAULT,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    minBufferSize * 4
            );
            try {
                LameModule.init(
                        sampleRate,
                        1,
                        sampleRate,
                        bitRate,
                        quality
                );
            } catch (Exception e) {
                throw new Exception(stringer.getString(
                        R.string.error_init_recorder
                ));
            }
            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                String logMessage = stringer.getString(
                        R.string.recording_mp3_at,
                        bitRate,
                        sampleRate
                );
                events.onNext(new Message(logMessage));
                return recorder;

            } else {
                throw new Exception(stringer.getString(
                        R.string.error_init_recorder
                ));
            }

        } else {
            throw new Exception(stringer.getString(
                    R.string.audioRecord_bad_value
            ));
        }
    }
}
