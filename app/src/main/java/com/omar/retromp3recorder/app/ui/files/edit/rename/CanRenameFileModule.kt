package com.omar.retromp3recorder.app.ui.files.edit.rename

import com.omar.retromp3recorder.state.repos.CanRenameFileRepo
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