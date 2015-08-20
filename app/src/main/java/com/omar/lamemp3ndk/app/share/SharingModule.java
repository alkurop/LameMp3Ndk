package com.omar.lamemp3ndk.app.share;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.omar.lamemp3ndk.app.controllers.ILsdDisplay;
import com.omar.lamemp3ndk.app.utils.ContextHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 20.08.15.
 */


public class SharingModule implements IShadingModule {
    private static final String SUBJECT = "Retro mp3 recording";
    private static final String TEXT = "mp3 file is included";
    private String filePath;

    public static SharingModule I(){return new SharingModule();}
    @Override
    public void StartShading(String _filePath, ILsdDisplay _display){
        filePath = _filePath;
        if(checkFile(filePath)){
            share();
            _display.SetText("Trying to share");
        }else{
            _display.SetText("Cannot share: file not detected");
        }
    }
    private boolean checkFile (String _filePath) {

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

    private List<LabeledIntent> formShareChoise(Intent emailIntent, PackageManager pm, ArrayList<Uri> utiList,
            List<ResolveInfo> resInfo) {



        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        String packageName = null;
        ResolveInfo ri;

        for (int i = 0; i < resInfo.size(); i++) {

            ri = resInfo.get(i);
            packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains("twitter") || packageName.contains("facebook.orca") || packageName.contains("skype") || packageName.contains("instagram") || packageName.contains("viber") || packageName.contains("dropbox") || packageName.contains("android.gm")) {

                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("text/plain");
                if (packageName.contains("android.gm")) {
                    intent.putExtra(Intent.EXTRA_TEXT, TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType("message/rfc822");

                }

                if (packageName.contains("dropbox")) {
                    intent.putExtra(Intent.EXTRA_TEXT, TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType("message/rfc822");

                }

                if (packageName.contains("skype")) {
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType("message/rfc822");

                }

                if (packageName.contains("viber")) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType("message/rfc822");

                }
                if (packageName.contains("facebook.orca")) {
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + TEXT + "\n");
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                    intent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, utiList);
                    intent.setType("text/pain");
                }

                if (packageName.contains("twitter")) {
                    intent.setAction(Intent.ACTION_SEND);
                    if (utiList.size() > 0) {
                        intent.putExtra(Intent.EXTRA_STREAM, utiList.get(0));
                    }
                    intent.putExtra(Intent.EXTRA_TEXT, SUBJECT + "\n" + TEXT);
                    intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);

                    intent.setType("text/pain");

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
        emailIntent.setType("message/rfc822");

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("*/*");
        PackageManager pm = ContextHelper.GetContext().getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        Intent openInChooser = Intent.createChooser(emailIntent, "Select");

        List<LabeledIntent> intentList = formShareChoise(emailIntent, pm,  audUris, resInfo);

        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        openInChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ContextHelper.GetContext().startActivity(openInChooser );
    }


}
