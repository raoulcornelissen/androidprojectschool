package com.congreats.stoppolitie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nickvankesteren on 14-10-16.
 */

public class ScannerActivity extends AppCompatActivity {
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner_layout);
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
			PostLicensePlate(photo);
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

	private String readInputStreamToString(HttpURLConnection connection) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;

		try {
			is = new BufferedInputStream(connection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			result = sb.toString();
		}
		catch (Exception e) {
			Log.i("error", "Error reading InputStream");
			result = null;
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException e) {
					Log.i("error", "Error closing InputStream");
				}
			}
		}

		return result;
	}

	private void PostLicensePlate(Bitmap image){
		try {
			String rawData = "licensePlateImage="+encodeToBase64(image);
			String type = "application/x-www-form-urlencoded";
			String encodedData = URLEncoder.encode(rawData);
			URL u = new URL("http://188.166.110.81/kentekenscanner/kenteken.php");
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", type);
			conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
			OutputStream os = conn.getOutputStream();
			os.write(encodedData.getBytes());

			Log.e("TEST", readInputStreamToString(conn));
			Toast.makeText(this, "Kenteken succesvol opgezocht!", Toast.LENGTH_SHORT).show();
		}catch(Exception ex){
			Log.e("error", "Error posting image");
			Toast.makeText(this, "Er is iets fout gegaan tijdens het uploaden van de foto!", Toast.LENGTH_LONG).show();
		}
	}
}
