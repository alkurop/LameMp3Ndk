package com.omar.retromp3recorder.app.di;

import dagger.Subcomponent;


@Subcomponent(modules = ConfigModule.class)
public interface ConfigSubComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(ConfigModule module);
        ConfigSubComponent build();
    }
}
