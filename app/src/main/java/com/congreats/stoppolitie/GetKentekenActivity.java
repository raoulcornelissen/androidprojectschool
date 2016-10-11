package com.congreats.stoppolitie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by raoul on 10-10-2016.
 */

public class GetKentekenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getkenteken_activity);

    }
    public void getsearch(View view){
        EditText input_kenteken =  (EditText)findViewById(R.id.kentekenbox);
        String kenteken = input_kenteken.getText().toString();
        try {
            String response = new RetrieveFeedTask(kenteken).execute().get();
            try {
                JSONArray array = (JSONArray) new JSONTokener(response).nextValue();
                JSONObject object = (JSONObject) array.getJSONObject(0);
                //Kenteken
                String kentekentext = object.getString("kenteken");
                TextView kentekenfield = (TextView)findViewById(R.id.kentekentextfield);
                kentekenfield.setText(kentekentext);
                //Merk van auto
                String merktext = object.getString("merk");
                TextView merktextfield = (TextView)findViewById(R.id.merktext);
                merktextfield.setText(merktext);
                //Model van auto
                String modeltext = object.getString("handelsbenaming");
                TextView modeltextfield = (TextView)findViewById(R.id.modeltext);
                modeltextfield.setText(modeltext);
                //Eerste kleur van auto
                String kleurtext = object.getString("eerste_kleur");
                TextView kleurtextfield = (TextView)findViewById(R.id.eerstekleur);
                kleurtextfield.setText(kleurtext);
                //Tweede kleur van auto
                String kleurtext2 = object.getString("tweede_kleur");
                TextView kleurtextfield2 = (TextView)findViewById(R.id.tweedekleur);
                kleurtextfield2.setText(kleurtext2);
                //Categorie van auto
                String categorietext = object.getString("europese_voertuigcategorie");
                TextView categorietextfield = (TextView)findViewById(R.id.category);
                categorietextfield.setText(categorietext);
            } catch (JSONException e) {
                // Appropriate error handling code
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    private Exception exception;
    String kenteken;
    public RetrieveFeedTask(String kenteken){
        super();
        this.kenteken = kenteken;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {
        String API_URL = "https://opendata.rdw.nl/resource/m9d7-ebf2.json";
        // Do some validation here

        try {
            URL url = new URL(API_URL + "?kenteken=" + this.kenteken);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    /* protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }

        return response;
    } */

}