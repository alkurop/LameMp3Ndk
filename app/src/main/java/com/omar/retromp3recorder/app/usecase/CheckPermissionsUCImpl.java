package com.omar.retromp3recorder.app.usecase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.utils.NotUnitTestable;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

@NotUnitTestable
public class CheckPermissionsUCImpl implements CheckPermissionsUC {
    private final Context context;
    private final RequestPermissionsRepo requestPermissionsRepo;

    @Inject
    public CheckPermissionsUCImpl(Context context, RequestPermissionsRepo requestPermissionsRepo) {
        this.context = context;
        this.requestPermissionsRepo = requestPermissionsRepo;
    }

    public Completable execute(Set<String> permissions) {
        return Observable
                .fromCallable(() -> {
                    Set<String> requestPermissions = new HashSet<>();
                    for (String permission : permissions) {
                        boolean hasPermission = ContextCompat
                                .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
                        if (!hasPermission) {
                            requestPermissions.add(permission);
                        }
                    }
                    return requestPermissions.isEmpty() ? new RequestPermissionsRepo.No()
                            : new RequestPermissionsRepo.Yes(requestPermissions);
                })
                .flatMapCompletable(shouldRequestPermissions -> Completable.fromAction(() ->
                        requestPermissionsRepo.newValue(shouldRequestPermissions))
                );
    }
}
