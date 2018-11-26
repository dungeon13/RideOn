package com.example.avin.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.Spinner;
import android.widget.TextView;
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

public class Booking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    getResponse response = new getResponse();
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Button button  = findViewById(R.id.submit);
        TextView tv1 = findViewById(R.id.user);
        final Spinner spinner1 = findViewById(R.id.sp1);
        spinner1.setOnItemSelectedListener(this);
        final Spinner spinner2 = findViewById(R.id.sp2);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.points,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        //get username from the login activity
        Bundle b2 = getIntent().getExtras();
        user = b2.getString("user");
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        //user = sharedPreferences.getString(PREF_USERNAME,null);
        tv1.setText(user);
        Bundle b3 = new Bundle();
        b3.putString("user",user);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg1 = spinner1.getSelectedItem().toString();
                String msg2 = spinner2.getSelectedItem().toString();
                JSONObject jsonObject = new JSONObject();
                if(!msg1.equals(msg2)) {
                    try {
                        jsonObject.put("roll", user);
                        jsonObject.put("start", msg1);
                        jsonObject.put("end", msg2);
                        response.execute(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Destination and Starting Points Same",Toast.LENGTH_SHORT);
                    toast.show();
                    showbook2();
                }
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id){
        //On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0){
        //To
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }
    public void onStart(){
        super.onStart();
        TextView view = (TextView)findViewById(R.id.user);
        view.setText("Welcome "+user);
    }
    public void logout(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        user="";
        //show login form
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    public void showbook(){
        // display book activity
        Bundle b4 = new Bundle();
        b4.putString("user",user);
        Intent intent = new Intent(this,Timer.class);
        intent.putExtras(b4);
        startActivity(intent);
    }
    public void showbook2(){
        // display book activity
        Bundle b = new Bundle();
        b.putString("user",user);
        Intent intent = new Intent(this,Booking.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    public  class getResponse extends AsyncTask<String,Void,String> {
        String js="0";
        String roll,password;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String ip="13.233.150.175";  //13.233.150.175
                int port = 8080;
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

           if(s.equals("2")){  //
                Toast toast = Toast.makeText(getApplicationContext(),"Booked",Toast.LENGTH_SHORT);
                toast.show();
                showbook();

            }
            else if(s.equals("1")){
                Toast toast = Toast.makeText(getApplicationContext(),"Cycle not Available ",Toast.LENGTH_SHORT);
                toast.show();
                showbook2();
            }
            else if(s.equals("3")){
                Toast toast = Toast.makeText(getApplicationContext(),"Sorry, Some Thing unusual happen",Toast.LENGTH_SHORT);
                toast.show();
                showbook2();
            }
            else if(s.equals("4")){
                Toast toast = Toast.makeText(getApplicationContext(),"You have already booked a cycle",Toast.LENGTH_SHORT);
                toast.show();
                showbook2();
            }
            else{
               showbook2();
           }





        }
    }
}

