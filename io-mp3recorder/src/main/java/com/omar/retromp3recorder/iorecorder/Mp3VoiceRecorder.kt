package com.omar.retromp3recorder.iorecorder

import android.media.AudioFormat
import com.github.alkurop.stringerbell.Stringer
import io.reactivex.rxjava3.core.Observable

interface Mp3VoiceRecorder {
    fun observeEvents(): Observable<Event>
    fun record(props: RecorderProps)
    fun stopRecord()
    fun isRecording(): Boolean

    enum class SampleRate(val value: Int) {
        _44100(44100), _22050(22050), _11025(11025), _8000(8000);
    }

    enum class BitRate(val value: Int) {
        _320(320), _192(192), _160(160), _128(128);
    }

    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
    }

    data class RecorderProps(
        val filepath: String,
        val bitRate: BitRate,
        val sampleRate: SampleRate,
    )

    companion object {
        val AUDIO_FORMAT_PRESETS = intArrayOf(
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val QUALITY_PRESETS = intArrayOf(0, 2, 5) // the lower the better
        val CHANNEL_PRESETS = intArrayOf(
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO
        )
    }

    fun observeState(): Observable<State>

    enum class State {
        Idle,
        Recording
    }

    fun observeRecorder(): Observable<ByteArray>
}
