package com.omar.retromp3recorder.app.ui.files.edit.rename

import dagger.Subcomponent

@CanRenameFileScope
@Subcomponent(modules = [CanRenameFileModule::class])
interface CanRenameFileComponent {
    fun inject(renameFileViewModel: RenameFileViewModel)
}