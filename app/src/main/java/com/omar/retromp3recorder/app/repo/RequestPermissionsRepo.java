package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.utils.OneShot;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class RequestPermissionsRepo {

    private final BehaviorSubject<OneShot<ShouldRequestPermissions>> state = BehaviorSubject.create();

    @Inject
    public RequestPermissionsRepo(){}

    public Observable<OneShot<ShouldRequestPermissions>> observe() {
        return state;
    }

    public void newValue(ShouldRequestPermissions newValue) {
        state.onNext(new OneShot<>(newValue));
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
