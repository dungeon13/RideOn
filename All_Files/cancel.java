package com.example.avin.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.example.avin.demo.Login.PREFS_NAME;
import static com.example.avin.demo.Login.PREF_USERNAME;

public class cancel extends AppCompatActivity {
    getResponse response = new getResponse();
     String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        Button button = findViewById(R.id.cancel);

        //SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        //user = sharedPreferences.getString(PREF_USERNAME,null);
        Bundle b7 = getIntent().getExtras();
        user = b7.getString("user");
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roll",user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                response.execute(jsonObject.toString());

            }
        });

    }
    public void showbook(){
        // display book activity
        Bundle b8 = new Bundle();
        b8.putString("user",user);
        Intent intent = new Intent(this,Booking.class);
        intent.putExtras(b8);
        startActivity(intent);
    }
    public  class getResponse extends AsyncTask<String,Void,String> {
        String js="0";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String ip="13.233.150.175"; //13.233.150.175
                int port = 7070;
                Socket s = new Socket(ip,port);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dos));
                writer.write(strings[0]);
                writer.write("\n");
                writer.flush();
                DataInputStream is = new DataInputStream(s.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                js = reader.readLine();
                writer.close();
                s.close();


            } catch (Exception e) {
                System.out.println(e);
            }
            return js;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // here the result is in the form of string s
            if(s.equals("1")){
                Toast toast = Toast.makeText(getApplicationContext(),"Ride Ended",Toast.LENGTH_SHORT);
                toast.show();
                showbook();
            }
        }
    }

}
