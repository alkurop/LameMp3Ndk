package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.state.repos.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional

class CanRenameFileRepo : BehaviorSubjectRepo<Optional<Boolean>>(Optional.empty())