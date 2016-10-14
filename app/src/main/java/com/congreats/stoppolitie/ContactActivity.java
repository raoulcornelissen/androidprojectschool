package com.congreats.stoppolitie;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by raoul on 10-10-2016.
 */

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.app_name)+"</font>"));
        setContentView(R.layout.contact_activity);
        LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.contact_activity);
        relativeLayout.setBackgroundColor(Color.rgb(57, 166, 178));
    }


}
