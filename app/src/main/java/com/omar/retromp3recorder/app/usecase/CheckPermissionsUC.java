package com.omar.retromp3recorder.app.usecase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class CheckPermissionsUC {
    private final Context context;
    private final RequestPermissionsRepo requestPermissionsRepo;

    public CheckPermissionsUC(Context context, RequestPermissionsRepo requestPermissionsRepo) {
        this.context = context;
        this.requestPermissionsRepo = requestPermissionsRepo;
    }

    public Completable execute(Set<String> permssions) {
        return Observable
                .fromCallable(() -> {
                    Set<String> requestPermissions = new HashSet<>();
                    for (String permissions : permssions) {
                        boolean hasPermission = ContextCompat
                                .checkSelfPermission(context, permissions) == PackageManager.PERMISSION_GRANTED;
                        if (!hasPermission) {
                            requestPermissions.add(permissions);
                        }
                    }
                    return requestPermissions.isEmpty() ? new RequestPermissionsRepo.No()
                            : new RequestPermissionsRepo.Yes(requestPermissions);
                })
                .flatMapCompletable(shouldRequestPermissions -> Completable.fromAction(
                        () -> requestPermissionsRepo.newValue(shouldRequestPermissions))
                );
    }
}
