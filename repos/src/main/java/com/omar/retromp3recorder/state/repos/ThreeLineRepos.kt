package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.recorder.RecorderDefaults
import com.omar.retromp3recorder.state.repos.common.BehaviorSubjectRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitRateRepo @Inject constructor(defaults: RecorderDefaults) :
    BehaviorSubjectRepo<Mp3VoiceRecorder.BitRate>(defaults.bitRate)

@Singleton
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<String>("")

@Singleton
class FileListRepo @Inject constructor() : BehaviorSubjectRepo<List<String>>()

@Singleton
class SampleRateRepo @Inject constructor(defaults: RecorderDefaults) :
    BehaviorSubjectRepo<Mp3VoiceRecorder.SampleRate>(defaults.sampleRate)