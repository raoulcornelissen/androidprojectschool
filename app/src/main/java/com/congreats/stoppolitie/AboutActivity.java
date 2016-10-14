package com.congreats.stoppolitie;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by raoul on 10-10-2016.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.app_name)+"</font>"));
        setContentView(R.layout.about_activity);
        LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.about_activity);
        relativeLayout.setBackgroundColor(Color.rgb(57, 166, 178));
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            TextView myAwesomeTextView = (TextView)findViewById(R.id.appversion);
            myAwesomeTextView.setText("" + version);
        }catch (Exception ex){

        }

    }


}
