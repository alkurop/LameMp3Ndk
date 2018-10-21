package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.repo.FileNameRepo;

import io.reactivex.Completable;

public class ShareUC {
    private final FileNameRepo repo;

    public ShareUC(FileNameRepo repo) {
        this.repo = repo;
    }

    public Completable execute() {
        return Completable.complete();
    }
}
