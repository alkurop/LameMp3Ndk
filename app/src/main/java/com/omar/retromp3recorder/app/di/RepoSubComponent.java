package com.omar.retromp3recorder.app.di;

import dagger.Subcomponent;


@Subcomponent(modules = ConfigModule.class)
public interface RepoSubComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(ConfigModule module);
        RepoSubComponent build();
    }
}
