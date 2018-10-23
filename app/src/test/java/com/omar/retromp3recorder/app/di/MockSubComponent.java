package com.omar.retromp3recorder.app.di;

import dagger.Subcomponent;

@Subcomponent(modules = {MockModule.class})
public interface MockSubComponent {
    @Subcomponent.Builder
    interface Builder {
        MockSubComponent.Builder requestModule(MockModule module);
        MockSubComponent build();
    }
}
