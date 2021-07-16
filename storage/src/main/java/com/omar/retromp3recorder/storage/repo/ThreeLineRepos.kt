package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.RecorderDefaults
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitRateRepo @Inject constructor(defaults: RecorderDefaults) :
    BehaviorSubjectRepo<Mp3VoiceRecorder.BitRate>(defaults.bitRate)

@Singleton
class CurrentFileRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<String>>(Optional.empty())

@Singleton
class FileListRepo @Inject constructor() :
    BehaviorSubjectRepo<List<ExistingFileWrapper>>(emptyList())

@Singleton
class SampleRateRepo @Inject constructor(defaults: RecorderDefaults) :
    BehaviorSubjectRepo<Mp3VoiceRecorder.SampleRate>(defaults.sampleRate)

@Singleton
class WavetableRepo @Inject constructor() :
    BehaviorSubjectRepo<Pair<String, Wavetable>>()

@Singleton
class SeekRepo @Inject constructor() :
    BehaviorSubjectRepo<Optional<Int>>(Optional.empty())

@Singleton
class JoinedProgressRepo @Inject constructor() :
    BehaviorSubjectRepo<JoinedProgress>(JoinedProgress.Hidden)
