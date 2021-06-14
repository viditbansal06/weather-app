package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText editText;
    public void answer(View view) {
        DownloadTask task = new DownloadTask();
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        try {
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() +"&appid=439d4b804bc8187953eb36d2a8c26a02");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        tv = (TextView) findViewById(R.id.tv);

    }



    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = " ";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result = result + current;
                    data = reader.read();
                }
                return result;


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_SHORT).show();
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String temp=jsonObject.getString("main");




                JSONArray a = new JSONArray(weatherInfo);


                String main = "";


                String description = "";

                String message = "";
                String temperature="";
                String pressure="";
                String max="";
                String min="";

                JSONObject jo=new JSONObject(temp);
                temperature=jo.getString("temp");
                JSONObject joo=new JSONObject(temp);
                pressure=joo.getString("pressure");
                max=joo.getString("temp_max");
                min=joo.getString("temp_min");








                for (int i = 0; i < a.length(); i++) {
                    JSONObject jsonPart = a.getJSONObject(i);
                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");
                    if (!main.equals("") && !description.equals("")) {
                        message = message + "1: main:" + main  +  "\r\n" +  "2: description:" + description +"\r\n" + "3: pressure:" + pressure   +"\r\n" + "4: temperature:" + temperature + "\r\n"  +"5: max temp:" + max +"\r\n" + "6: min temp:" + min + "\r\n" ;
                    }


                }


                if (!message.equals("")) {
                    tv.setText(message);

                }

                else
                {
                    Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}