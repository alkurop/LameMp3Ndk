package com.omar.retromp3recorder.app.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.controllers.ILsdDisplay;
import com.omar.retromp3recorder.app.utils.ContextHelper;

import java.io.File;

/**
 * Created by omar on 20.08.15.
 */


public class OldSharingModule implements IShadingModule {
    private Context context ;

    private String filePath;

    public static OldSharingModule I() {
        return new OldSharingModule();
    }

    @Override
    public void startShading(String _filePath, ILsdDisplay _display) {
        context = ContextHelper.getContext();

        filePath = _filePath;
        if (checkFile(filePath)) {
            share();
            _display.setText(context.getString(R.string.trying_to_share));
        } else {
            _display.setText(context.getString(R.string.cannot_share));
        }
    }

    private boolean checkFile(String _filePath) {

        File file = new File(_filePath);
        return file.exists();
    }

    private void share() {
        initShareIntent(collectForShare());

    }

    private Uri collectForShare() {
        File fileIn = new File(filePath);
        fileIn.setReadable(true, false);
       Uri u = Uri.fromFile(fileIn);
        return u;

    }



    private void initShareIntent(Uri u) {



        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, u);
        shareIntent.setType("audio/mpeg4-generic");
        context.  startActivity(Intent.createChooser(shareIntent, context.getString(R.string.select)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


}
