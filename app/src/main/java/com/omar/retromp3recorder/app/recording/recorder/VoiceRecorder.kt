package com.omar.retromp3recorder.app.recording.recorder

import android.media.AudioFormat
import com.github.alkurop.stringerbell.Stringer
import io.reactivex.Observable

interface VoiceRecorder {
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
        val sampleRate: SampleRate
    )

    companion object {
        val AUDIO_FORMAT_PRESETS = shortArrayOf(
            AudioFormat.ENCODING_PCM_8BIT.toShort(),
            AudioFormat.ENCODING_PCM_16BIT
                .toShort()
        )
        val QUALITY_PRESETS = intArrayOf(
            2,
            5,
            7
        ) // the lower the better
        val CHANNEL_PRESETS = shortArrayOf(
            AudioFormat.CHANNEL_IN_MONO.toShort(),
            AudioFormat.CHANNEL_IN_STEREO
                .toShort()
        )
    }
}
