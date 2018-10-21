package com.omar.retromp3recorder.app.repo;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RequestPermissionsRepo {

    private final PublishSubject<ShouldRequestPermissions> state = PublishSubject.create();

    public Observable<ShouldRequestPermissions> observe() {
        return state;
    }

    public void newValue(ShouldRequestPermissions newValue) {
        state.onNext(newValue);
    }

    public interface ShouldRequestPermissions { }

    public static class No implements ShouldRequestPermissions { }

    public static class Yes implements ShouldRequestPermissions {
        public final Set<String> permissions;

        public Yes(Set<String> permissions) {
            this.permissions = permissions;
        }
    }
}
