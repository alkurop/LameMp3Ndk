package com.omar.retromp3recorder.app.ui.files.rename

import com.omar.retromp3recorder.storage.repo.CanRenameFileRepo
import dagger.Module
import dagger.Provides

@Module
class CanRenameFileModule {
    @CanRenameFileScope
    @Provides
    fun provideRepo(): CanRenameFileRepo {
        return CanRenameFileRepo()
    }
}