package com.congreats.kentekenscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	String kenteken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.app_name)+"</font>"));
        setContentView(R.layout.getkenteken_activity);
        Intent intent = getIntent();
	    if(intent.hasExtra("kenteken")){
		    kenteken = intent.getStringExtra("kenteken");
            EditText input_kenteken = (EditText) findViewById(R.id.kentekenbox);
            input_kenteken.setText(kenteken.toUpperCase());
            searchFunc();
		    Button button = (Button) findViewById(R.id.btnScanAgain);
		    button.setVisibility(View.VISIBLE);
	    }else{
		    Button button = (Button) findViewById(R.id.btnScanAgain);
		    button.setVisibility(View.GONE);
	    }
        LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.getkenteken_activity);
        relativeLayout.setBackgroundColor(Color.rgb(57, 166, 178));

    }
    public void getsearch(View view) {
        searchFunc();
    }

	public void scanAgain(View view) {
		Intent intent = new Intent(this, ScannerActivity.class);
		startActivity(intent);
	}

    private void searchFunc(){
        EditText input_kenteken = (EditText) findViewById(R.id.kentekenbox);
        String kenteken = input_kenteken.getText().toString().toUpperCase();
        int kentekenlengte = kenteken.length();
        if (kentekenlengte != 6) {
            Toast.makeText(this, "Er is een fout opgetreden!", Toast.LENGTH_LONG).show();
        } else {
            try {
                String response = new RetrieveFeedTask(kenteken).execute().get();
                try {

                    JSONArray array = (JSONArray) new JSONTokener(response).nextValue();
                    JSONObject object = (JSONObject) array.getJSONObject(0);
                    //Kenteken
                    String kentekentext = object.getString("kenteken");
                    TextView kentekenfield = (TextView) findViewById(R.id.kentekentextfield);
                    kentekenfield.setText(kentekentext);
                    //Merk van auto
                    String merktext = object.getString("merk");
                    TextView merktextfield = (TextView) findViewById(R.id.merktext);
                    merktextfield.setText(merktext);
                    //Model van auto
                    String modeltext = object.getString("handelsbenaming");
                    TextView modeltextfield = (TextView) findViewById(R.id.modeltext);
                    modeltextfield.setText(modeltext);
                    //Eerste kleur van auto
                    String kleurtext = object.getString("eerste_kleur");
                    TextView kleurtextfield = (TextView) findViewById(R.id.eerstekleur);
                    kleurtextfield.setText(kleurtext);
                    //Tweede kleur van auto
                    String kleurtext2 = object.getString("tweede_kleur");
                    TextView kleurtextfield2 = (TextView) findViewById(R.id.tweedekleur);
                    kleurtextfield2.setText(kleurtext2);
                    //Categorie van auto
                    String categorietext = object.getString("europese_voertuigcategorie");
                    TextView categorietextfield = (TextView) findViewById(R.id.category);
                    //https://www.rdw.nl/sites/tgk/Paginas/Voertuigcategorie%C3%ABn.aspx <-- Hier komt alle informatie van de categorien vandaan
                    if(categorietext.equals("M1") || categorietext.equals("M2") || categorietext.equals("M3"))
                    {
                        categorietextfield.setText("Personenautos en Bussen");
                    }else if(categorietext.equals("N1") || categorietext.equals("N2") || categorietext.equals("N3"))
                    {
                        categorietextfield.setText("Vrachtautos");
                    }else if(categorietext.equals("O1") || categorietext.equals("O2") || categorietext.equals("O3") || categorietext.equals("O4"))
                    {
                        categorietextfield.setText("Aanhangwagens");
                    }else if(categorietext.equals("L1e") || categorietext.equals("L2e") || categorietext.equals("L3e") || categorietext.equals("L4e") || categorietext.equals("L5e") || categorietext.equals("L6e") || categorietext.equals("L7e"))
                    {
                        categorietextfield.setText("Twee- en driewielige voertuigen");
                    }else if(categorietext.equals("T1") || categorietext.equals("T2") || categorietext.equals("T3") || categorietext.equals("T4") || categorietext.equals("T5"))
                    {
                        categorietextfield.setText("Trekkers op wielen");
                    }else if (categorietext.equals("C"))
                    {
                        categorietextfield.setText("Trekkers op rupsbanden");
                    }else if(categorietext.equals("R1") || categorietext.equals("R2") || categorietext.equals("R3") || categorietext.equals("R4"))
                    {
                        categorietextfield.setText("Aanhangwagen t.b.v. land-of bosbouw");
                    }else if(categorietext.equals("S1") || categorietext.equals("S2"))
                    {
                        categorietextfield.setText("Verwisselbare getrokken machines");
                    }else
                    {
                        categorietextfield.setText(categorietext);
                    }
                    //Toast.makeText(this, "Het is gelukt om de informatie op te halen!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Er is een fout opgetreden!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Dit kenteken bestaat niet!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    String kenteken;
    public RetrieveFeedTask(String kenteken){
        super();
        this.kenteken = kenteken;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(Void... urls) {
        String API_URL = "https://opendata.rdw.nl/resource/m9d7-ebf2.json";
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
}