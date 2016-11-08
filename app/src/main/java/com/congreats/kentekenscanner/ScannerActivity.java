package com.congreats.kentekenscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nickvankesteren on 14-10-16.
 */

public class ScannerActivity extends AppCompatActivity {
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>"+getString(R.string.app_name)+"</font>"));
		setContentView(R.layout.scanner_layout);
		LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.linScanner);
		relativeLayout.setBackgroundColor(Color.rgb(57, 166, 178));

		this.imageView = (ImageView)this.findViewById(R.id.imageView);
		Button photoButton = (Button) this.findViewById(R.id.button);
		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);
			new PostLicensePlate(this).execute(photo);
		}
	}

	private static String encodeToBase64(Bitmap image){
		System.gc();
		if(image == null)return null;

		Bitmap photo = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		byte[] b = baos.toByteArray();

		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		return imageEncoded;
	}

	private class PostLicensePlate extends AsyncTask<Bitmap, Void, String>{
		Context context;

		public PostLicensePlate(Context context){
			this.context = context;
		}

		@Override
		protected String doInBackground(Bitmap... images) {
			try {
				URL u = new URL("http://188.166.110.81/kentekenscannerSchool/kenteken.php");
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				HashMap<String, String> postDataParams = new HashMap<>();
				postDataParams.put("licensePlateImage", encodeToBase64(images[0]));
				writer.write(getPostDataString(postDataParams));
				writer.flush();
				writer.close();
				os.close();
				String response = "";
				int responseCode=conn.getResponseCode();

				if(responseCode == HttpURLConnection.HTTP_OK){
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while ((line = br.readLine()) != null) {
						response += line;
					}
				}else{
					response = "---";
				}

				return response;
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return "error";
		}

		private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();
			boolean first = true;
			for(Map.Entry<String, String> entry : params.entrySet()){
				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}

			return result.toString();
		}

		@Override
		protected void onPostExecute(String kenteken) {
			if(kenteken.startsWith("Exception when calling DefaultApi->recognizePost:")) {
				Toast.makeText(ScannerActivity.this, "Er is een fout opgetreden met het ophalen van het kenteken!", Toast.LENGTH_LONG).show();
			}else if(kenteken.startsWith("no_license_plate")){
				Toast.makeText(ScannerActivity.this, "Er is geen geldig kenteken gevonden!", Toast.LENGTH_LONG).show();
			}else{
				TextView textView = (TextView) findViewById(R.id.textView);
				textView.setText(kenteken);
				Intent intent = new Intent(this.context, GetKentekenActivity.class);
				intent.putExtra("kenteken", kenteken);
				startActivity(intent);
			}
			super.onPostExecute(kenteken);
		}
	}
}
