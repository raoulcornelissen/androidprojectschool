package com.congreats.kentekenscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    public void sendContact(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kentekenscanner@raoulcornelissen.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Contact opnemen vanuit de Kenteken Scanner app");
        try {
            startActivity(Intent.createChooser(i, "Verstuur e-mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Er is geen e-mail client geinstalleerd!", Toast.LENGTH_SHORT).show();
        }
    }
}
