package com.congreats.stoppolitie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.over:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.email");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
                return true;
            case R.id.contact:
                Intent launchIntent2 = getPackageManager().getLaunchIntentForPackage("com.android.camera");
                if (launchIntent2 != null) {
                    startActivity(launchIntent2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
