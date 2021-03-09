package com.omar.retromp3recorder.app.recording.recorder

import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.recorder.LameModule
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.Companion.AUDIO_FORMAT_PRESETS
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.Companion.CHANNEL_PRESETS
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.Companion.QUALITY_PRESETS
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import com.omar.retromp3recorder.app.utils.NotUnitTestable
import com.omar.retromp3recorder.app.utils.disposedBy
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@NotUnitTestable
class VoiceRecorderRX @Inject internal constructor(
    private val scheduler: Scheduler,
    private val context: Context
) : VoiceRecorder, StatefulVoiceRecorder {
    private val events: Subject<VoiceRecorder.Event> = PublishSubject.create()
    private val elapsed = AtomicLong(0)
    private val compositeDisposable = CompositeDisposable()

    private val state = BehaviorSubject.createDefault(StatefulVoiceRecorder.State.Idle)

    override fun observeState(): Observable<StatefulVoiceRecorder.State> = state

    override fun observeEvents(): Observable<VoiceRecorder.Event> {
        return events
    }

    override fun record(props: RecorderProps) {
        val minBufferSize = AudioRecord.getMinBufferSize(
            props.sampleRate.value,
            channelConfig.toInt(),
            audioFormat.toInt()
        )
        createOutputFile(props.filepath)
            .zipWith(
                createRecorder(minBufferSize, props.sampleRate.value, props.bitRate.value),
                { file, audioRecord -> Pair(file, audioRecord) }
            )
            .flatMapCompletable { fileAudioRecordPair ->
                record(
                    fileAudioRecordPair.first,
                    fileAudioRecordPair.second,
                    props.sampleRate.value
                )
            }
            .onErrorResumeNext { throwable ->
                events.onNext(
                    VoiceRecorder.Event.Error(
                        Stringer.ofString(
                            throwable.message ?: throwable.toString()
                        )
                    )
                )
                Timber.e(throwable)
                Completable.complete()
            }
            .subscribeOn(scheduler)
            .doOnSubscribe { state.onNext(StatefulVoiceRecorder.State.Recording) }
            .doFinally { state.onNext(StatefulVoiceRecorder.State.Idle) }
            .subscribe()
            .disposedBy(compositeDisposable)
    }

    override fun isRecording(): Boolean = state.blockingFirst() == StatefulVoiceRecorder.State.Recording

    override fun stopRecord() {
        compositeDisposable.clear()
        state.onNext(StatefulVoiceRecorder.State.Idle)
    }

    private fun createBuffer(sampleRate: Int): ShortArray {
        return ShortArray(sampleRate * (16 / 8) * 5)
    }

    private fun createMp3Buffer(buffer: ShortArray): ByteArray {
        return ByteArray((7200 + buffer.size * 2 * 1.25).toInt())
    }

    private fun createRecorder(
        minBufferSize: Int,
        sampleRate: Int,
        bitRate: Int
    ): Single<AudioRecord> {
        return Single.fromCallable {
            findAudioRecord(
                minBufferSize,
                sampleRate, bitRate
            )
        }
    }

    private fun createOutputFile(filePath: String): Single<File> {
        return Single.fromCallable {
            val outFile = File(filePath)
            if (outFile.exists()) {
                outFile.delete()
            }
            val fileWasCreated = outFile.createNewFile()
            if (!fileWasCreated) {
                throw IOException(
                    context.getString(
                        R.string.file_was_not_created,
                        filePath
                    )
                )
            }
            outFile
        }
    }

    private fun record(outFile: File, recorder: AudioRecord, sampleRate: Int): Completable {
        return Completable
            .create { emitter: CompletableEmitter ->
                try {
                    elapsed.set(System.currentTimeMillis())
                    Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
                    val output = FileOutputStream(outFile)
                    emitter.setCancellable {
                        recorder.stop()
                        recorder.release()
                        LameModule.close()
                        output.close()
                        sendFinishLog(outFile)
                    }
                    val buffer = createBuffer(sampleRate)
                    val mp3Buffer = createMp3Buffer(buffer)
                    val minBufferSize = AudioRecord.getMinBufferSize(
                        sampleRate,
                        channelConfig.toInt(),
                        audioFormat.toInt()
                    )
                    recorder.startRecording()
                    var readSize: Int
                    while (!emitter.isDisposed) {
                        readSize = recorder.read(
                            buffer,
                            0,
                            minBufferSize
                        )
                        val encResult = LameModule.encode(
                            buffer,
                            buffer,
                            readSize,
                            mp3Buffer
                        )
                        output.write(mp3Buffer, 0, encResult)
                    }
                    val flushResult = LameModule.flush(mp3Buffer)
                    if (flushResult != 0) {
                        output.write(mp3Buffer, 0, flushResult)
                    }
                } catch (e: Throwable) {
                    emitter.tryOnError(e)
                }
            }
    }

    private fun sendFinishLog(outFile: File?) {
        val counter = System.currentTimeMillis() - elapsed.get()
        val absolutePath = outFile!!.absolutePath
        if (outFile.exists()) {
            val messages = arrayOf(
                Stringer(R.string.file_saved_to, absolutePath),
                Stringer(R.string.audio_length, counter.toFloat() / 1000),
                Stringer(R.string.file_size, outFile.length().toFloat() / 1000),
                Stringer(R.string.compression_rate, outFile.length().toFloat() / counter)
            )
            for (message in messages) {
                events.onNext(VoiceRecorder.Event.Message(message))
            }
        } else events.onNext(
            VoiceRecorder.Event.Error(
                Stringer(R.string.error_saving_file_to, absolutePath)
            )
        )
    }

    @Throws(Exception::class)
    private fun findAudioRecord(
        minBufferSize: Int,
        sampleRate: Int,
        bitRate: Int
    ): AudioRecord {
        return if (minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
            val recorder = AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRate,
                channelConfig.toInt(),
                audioFormat.toInt(),
                minBufferSize * 4
            )
            try {
                LameModule.init(
                    sampleRate,
                    1,
                    sampleRate,
                    bitRate,
                    quality
                )
            } catch (e: Exception) {
                throw Exception(
                    context.getString(
                        R.string.error_init_recorder
                    )
                )
            }
            if (recorder.state == AudioRecord.STATE_INITIALIZED) {
                val logMessage = Stringer(
                    R.string.recording_mp3_at,
                    bitRate,
                    sampleRate
                )
                events.onNext(VoiceRecorder.Event.Message(logMessage))
                recorder
            } else {
                throw Exception(
                    context.getString(
                        R.string.error_init_recorder
                    )
                )
            }
        } else {
            throw Exception(
                context.getString(
                    R.string.audioRecord_bad_value
                )
            )
        }
    }

    companion object {
        private val channelConfig: Short = CHANNEL_PRESETS.get(0)
        private val quality: Int = QUALITY_PRESETS.get(1)
        private val audioFormat: Short = AUDIO_FORMAT_PRESETS.get(1)
    }
}