package com.omar.retromp3recorder.bl.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class UsecaseModule {
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE)
    }
}