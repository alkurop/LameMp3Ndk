package com.omar.retromp3recorder.app.callbacks;

import com.omar.retromp3recorder.app.test.UnitTestBase;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Created by omar on 8/31/15.
 */
public class ImpossibleCallbackTest extends UnitTestBase {

   @Mock ImpossibleCallback mckImpossibleCallback;

    @Test
    public void testGetResponce() throws Exception {
        //init
        final String response = "success";
        final String fakeResponse = "fakeSuccess";


        Mockito.doCallRealMethod().when(mckImpossibleCallback).run();
        Mockito.doCallRealMethod().when(mckImpossibleCallback).getResponce(Mockito.any(ImpossibleCallback.ICallback.class));
        Mockito.doCallRealMethod().when(mckImpossibleCallback).getResponceString()  ;

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ImpossibleCallback.ICallback)invocation.getArguments()[0]).Success(response);
                return null;
            }
        }).when(mckImpossibleCallback).getResponce(Mockito.any(ImpossibleCallback.ICallback.class));


        //akt
        mckImpossibleCallback.run();

        //assert

        Assert.assertEquals(mckImpossibleCallback.getResponceString(), response);    }
}