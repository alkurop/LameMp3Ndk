package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.share.SharingModule;

import javax.inject.Inject;

import io.reactivex.Completable;

public class ShareUCImpl implements ShareUC {
    private final SharingModule sharingModule;

    @Inject
    public ShareUCImpl(SharingModule sharingModule) {
        this.sharingModule = sharingModule;
    }

    public Completable execute() {
        return sharingModule.share();
    }
}
