package com.omar.retromp3recorder.app.stringer;

import javax.inject.Inject;

public class TestStringer implements Stringer {

    @Inject
    public TestStringer(){}
    @Override
    public String getString(int stringRes) {
        return null;
    }

    @Override
    public String getString(int stringRes, Object... args) {
        return null;
    }
}
