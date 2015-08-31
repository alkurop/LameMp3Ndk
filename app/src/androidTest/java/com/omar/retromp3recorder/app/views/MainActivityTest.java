package com.omar.retromp3recorder.app.views;

import android.media.audiofx.Visualizer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.omar.retromp3recorder.app.customviews.VisualizerView;
import com.omar.retromp3recorder.app.presenters.IMainEvents;

import static junit.framework.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.Spy;

public class MainActivityTest extends TestCase {
    @Spy
    MainActivity  mckMainActivity;

   @Mock private ImageView btn_Play, btn_Record, btn_Share;
    @Mock private LinearLayout llLogHolder, llRadioContainer1, llRadioContainer2;
    @Mock private IMainEvents presenter;
    @Mock private ScrollView scrollView;
    @Mock  private TextView tv_Timer;
    @Mock   private VisualizerView mVisualizerView;
    @Mock private Visualizer mVisualizer;

@Test
    public void testSetUI() throws Exception {
        mckMainActivity.onCreate(null);
         mckMainActivity.SetUI();
        assertNotNull(btn_Play);
    }

    public void testSetRecordBtnImg() throws Exception {

    }

    public void testSetPlayBtnImg() throws Exception {

    }

    public void testStartVisualizer() throws Exception {

    }

    public void testStopVisualizer() throws Exception {

    }

    public void testSetLabelText() throws Exception {

    }

    public void testGetRadioContainer1() throws Exception {

    }

    public void testGetRadioContainer2() throws Exception {

    }

    public void testOnStop() throws Exception {

    }
}