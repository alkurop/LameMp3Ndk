package com.omar.retromp3recorder.app.repo;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class RequestPermissionsRepo {

    private final PublishSubject<ShouldRequestPermissions> state = PublishSubject.create();

    @Inject
    public RequestPermissionsRepo(){}

    public Observable<ShouldRequestPermissions> observe() {
        return state;
    }

    public void newValue(ShouldRequestPermissions newValue) {
        state.onNext(newValue);
    }

    //region events
    public interface ShouldRequestPermissions { }

    public static class No implements ShouldRequestPermissions { }

    public static class Yes implements ShouldRequestPermissions {
        public final Set<String> permissions;

        public Yes(Set<String> permissions) {
            this.permissions = permissions;
        }
    }
    //endregion
}
