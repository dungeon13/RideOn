package com.example.avin.demo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Login extends AppCompatActivity {

    public static String PREFS_NAME="mypre";
    public static String PREF_USERNAME="username";
    public static String PREF_PASSWORD="password";
    Button button;
    getResponse response = new getResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = findViewById(R.id.button);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public void onStart(){
        super.onStart();
        // read the username and password from shared preferences
        getUser();
    }
    public void doLogin(View view) throws JSONException {
        EditText roll=findViewById(R.id.roll);
        EditText pass=findViewById(R.id.pass);
        String rollValue,passValue;
        rollValue = roll.getText().toString();
        passValue = pass.getText().toString();
        JSONObject jsonObject = new JSONObject();
        int flag=0;
        if(!rollValue.equals("") && !passValue.equals("")) {
            jsonObject.put("roll", rollValue);
            jsonObject.put("pass", passValue);
            flag=1;
        }
        if(isOnline() && flag==1) {
            response.execute(jsonObject.toString());

        }
        else if(!isOnline() && flag==1){
            Toast toast = Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(flag==0){
            Toast toast = Toast.makeText(getApplicationContext(),"No Empty Fields",Toast.LENGTH_SHORT);
            toast.show();
        }




    }
    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = sharedPreferences.getString(PREF_USERNAME,null);
        String password = sharedPreferences.getString(PREF_PASSWORD,null);
        if(username != null || password != null){
            showbook(username);
        }
    }
    public void rememberMe(String user, String password){
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit().putString(PREF_USERNAME,user).putString(PREF_PASSWORD,password).commit();
    }
    public void showbook(String user){
        // display book activity
        Bundle b1 = new Bundle();
        b1.putString("user",user);
        Intent intent = new Intent(this,Booking.class);
        intent.putExtras(b1);
        startActivity(intent);
    }
    public void showbook2(String user){
        // display book activity
        Bundle b7 = new Bundle();
        b7.putString("user",user);
        Intent intent = new Intent(this,cancel.class);
        intent.putExtras(b7);
        startActivity(intent);
    }
    public void showthis(){
        // display book activity
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public  class getResponse extends AsyncTask<String,Integer,String> {
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
                int port = 9091;
                Socket s = new Socket(ip,port);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dos));
                writer.write(strings[0]);
                JSONObject jsonObject = new JSONObject(strings[0]);
                roll = jsonObject.getString("roll");
                password = jsonObject.getString("pass");
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
            if(js.equals("1")){  //
                CheckBox ch=findViewById(R.id.checkbox);
                if(ch.isChecked())
                    rememberMe(roll,password); //save username and password
                //show booking activity
                showbook(roll);

            }
            else if(js.equals("2")){
                CheckBox ch=findViewById(R.id.checkbox);
                if(ch.isChecked())
                    rememberMe(roll,password); //save username and password
                //show booking activity
                Toast toast = Toast.makeText(getApplicationContext(),"Already Booekd a Cycle",Toast.LENGTH_SHORT);
                toast.show();
                showbook2(roll);
            }

            else if(js.equals("0")){
                Toast toast = Toast.makeText(getApplicationContext(),"Wrong credentials",Toast.LENGTH_SHORT);
                toast.show();
                showthis();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"SomeThing Went Wrong",Toast.LENGTH_SHORT);
                toast.show();
            }



        }
    }

}
