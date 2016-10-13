package com.congreats.stoppolitie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
                Intent intent2 = new Intent(this, HelpActivity.class);
                startActivity(intent2);
                return true;
            case R.id.contact:
                Intent intent3 = new Intent(this, ContactActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getOpvragen(View view){
        Intent intent4 = new Intent(this, GetKentekenActivity.class);
        startActivity(intent4);
    }
    public void getToevoegen(View view){
        Intent intent5 = new Intent(this, AddKentekenActivity.class);
        startActivity(intent5);
    }
}
