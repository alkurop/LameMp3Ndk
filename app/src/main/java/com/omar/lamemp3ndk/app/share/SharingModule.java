package com.omar.lamemp3ndk.app.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.omar.lamemp3ndk.app.Constants;
import com.omar.lamemp3ndk.app.R;
import com.omar.lamemp3ndk.app.controllers.ILsdDisplay;
import com.omar.lamemp3ndk.app.utils.ContextHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 20.08.15.
 */


public class SharingModule implements IShadingModule {
    private Context context ;

    private String SUBJECT;
    private String TEXT;
    private String filePath;

    public static SharingModule I() {
        return new SharingModule();
    }

    @Override
    public void StartShading(String _filePath, ILsdDisplay _display) {
        context = ContextHelper.GetContext();
        SUBJECT = context. getString(R.string.retro_mp3_recordnig);
        TEXT = context.getString(R.string.mp3_file_is_included);

        filePath = _filePath;
        if (checkFile(filePath)) {
            share();
            _display.SetText(context.getString(R.string.trying_to_share));
        } else {
            _display.SetText(context.getString(R.string.cannot_share));
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

    private List<LabeledIntent> formShareChoise(Intent emailIntent, PackageManager pm, ArrayList<Uri> utiList, List<ResolveInfo> resInfo) {


        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        String packageName;
        ResolveInfo ri;

        for (int i = 0; i < resInfo.size(); i++) {

            ri = resInfo.get(i);
            packageName = ri.activityInfo.packageName;
            if (packageName.contains(Constants.ANDROID_EMAIL)) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains(Constants.TWEETER) || packageName.contains(Constants.FACEBOOK_ORCA) || packageName.contains(Constants.SKYPE) || packageName.contains(Constants.INSTAGRAM) || packageName.contains(Constants.VIBER) ||
                    packageName.contains(Constants.DROPBOX) || packageName.contains(Constants.ANDROID_GM)) {

                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType(Constants.TEXT_PLAIN);
                if (packageName.contains(Constants.ANDROID_GM)) {
                    intent.putExtra(Intent.EXTRA_TEXT, TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType(Constants.MESSAGE_RFC822);

                }

                if (packageName.contains(Constants.DROPBOX)) {
                    intent.putExtra(Intent.EXTRA_TEXT, TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType(Constants.MESSAGE_RFC822);

                }

                if (packageName.contains(Constants.SKYPE)) {
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType(Constants.MESSAGE_RFC822);

                }

                if (packageName.contains(Constants.VIBER)) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType(Constants.MESSAGE_RFC822);

                }
                if (packageName.contains(Constants.FACEBOOK_ORCA)) {
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + TEXT + "\n");
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType(Constants.TEXT_PLAIN);
                }

                if (packageName.contains(Constants.TWEETER)) {
                    intent.setAction(Intent.ACTION_SEND);
                    if (utiList.size() > 0) {
                        intent.putExtra(Intent.EXTRA_STREAM, utiList.get(0));
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);

                    intent.setType(Constants.TEXT_PLAIN);

                }
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));

            }
        }
        return intentList;

    }

    private void initShareIntent(Uri u) {

        ArrayList<Uri> audUris = new ArrayList<Uri>();
        audUris.add(u);

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, TEXT);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
        emailIntent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, audUris);
        emailIntent.setType(Constants.MESSAGE_RFC822);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(Constants.TYPE_ALL);
        PackageManager pm = ContextHelper.GetContext().getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        Intent openInChooser = Intent.createChooser(emailIntent, context.getString(R.string.select));

        List<LabeledIntent> intentList = formShareChoise(emailIntent, pm, audUris, resInfo);

        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        openInChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextHelper.GetContext().startActivity(openInChooser);
    }


}
