package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.dto.FileWrapper
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
    BehaviorSubjectRepo<List<FileWrapper>>(emptyList())

@Singleton
class SampleRateRepo @Inject constructor(defaults: RecorderDefaults) :
    BehaviorSubjectRepo<Mp3VoiceRecorder.SampleRate>(defaults.sampleRate)

