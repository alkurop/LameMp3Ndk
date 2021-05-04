package com.omar.retromp3recorder.storage.repo

import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.Optional

class CanRenameFileRepo : BehaviorSubjectRepo<Optional<Boolean>>(Optional.empty())