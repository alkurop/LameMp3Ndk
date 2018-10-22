package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class MainViewPresenterTest {
    @Inject
    MainViewPresenter presenter;

    @Before
    public void setUp() {
        DaggerTestAppComponent.builder().build().inject(this);
    }
    @Test
    public void test() {
        int i = 10;
    }
}
