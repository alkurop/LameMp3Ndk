package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {

    @Singleton
    @Provides
    public BitRateRepo provideBR() {
        return new BitRateRepo();
    }

    @Singleton
    @Provides
    public FileNameRepo provideFN() {
        return new FileNameRepo();
    }

    @Singleton
    @Provides
    public RequestPermissionsRepo provideRP() {
        return new RequestPermissionsRepo();
    }

    @Singleton
    @Provides
    public SampleRateRepo provideSR(){
        return new SampleRateRepo();
    }

    @Singleton
    @Provides
    public StateRepo provideS(){
        return new StateRepo();
    }
}
