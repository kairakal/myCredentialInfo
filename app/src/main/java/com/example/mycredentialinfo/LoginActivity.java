package com.example.mycredentialinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import com.karan.churi.PermissionManager.PermissionManager;
public class LoginActivity extends AppCompatActivity {

    //PermissionManager permissionManager;
    private String userEmail;
    private int userId;
    public static final String MY_PREF = "MyPreference";
    private String email, password;

    Button loginButton, signUpButton;

    EditText emailEditText, passwordEditText;
    RequestQueue queue,queuelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpViews();
       // serverTest();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if(email != null && password != null)
                {
                    //User user = new User(email, password);
                    login(email,password);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Login Details Empty!", Toast.LENGTH_SHORT).show();
                }

            }


        });
         signUpButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 email = emailEditText.getText().toString().trim();
                 password = passwordEditText.getText().toString().trim();


                 if(email != null && password != null)
                 {
                    // User user = new User(email, password);
                     signup( email,password);
                 }
                 else
                 {
                     Toast.makeText(LoginActivity.this, "SignUp Details Empty!", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }

   /* private void serverTest() {


        JSONObject testParams = new JSONObject();
        String testUrl = Constants.BASE_URL + Constants.TEST_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, testUrl, testParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject status = response.getJSONObject("");

                    Toast.makeText(LoginActivity.this, "ServerStatus : " +  status.get("status"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "ServerStatus : Down : " +error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }*/

    private void signup(final String email, String password) {
      /*  this.email =emailEditText.getText().toString().trim();
        if (this.email.equalsIgnoreCase("")) {
            emailEditText.setError("This field can not be left blank");
            emailEditText.setText("");
        }
        this.email =emailEditText.getText().toString().trim();
        EmailValidator emailValidator= new EmailValidator();
        if(!emailValidator.validate(this.email)){
            emailEditText.setError("This email entered is wrong");
            emailEditText.setText("");
        }
        this.email =emailEditText.getText().toString().trim();
        this.password =passwordEditText.getText().toString();
        if(this.password.equalsIgnoreCase("")){
        passwordEditText.setError("This field can not be left blank ");
        passwordEditText.setText("");
        }
        this.password =passwordEditText.getText().toString();
        if(this.password.length()<5){
            passwordEditText.setError("Password should  atleast have length 5");
            passwordEditText.setText("");
        }
        this.password =passwordEditText.getText().toString();*/
        Map<String,String> params= new HashMap<>();
        params.put("email", this.email);
        params.put("password", this.password);
        JSONObject signupParams = new JSONObject(params);
        String signUrl = Constants.BASE_URL + Constants.SIGNUP_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, signUrl, signupParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                     userId = data.getInt("id");
                    //Toast.makeText(LoginActivity.this, "User signup successfully  with id " + signupid, Toast.LENGTH_SHORT).show();

                    userEmail = data.getString("email");

                    Toast.makeText(LoginActivity.this, "Thanks for joining us.\nYour unique ID is: " + userId + ".\nPlease fill the details to continue", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREF, MODE_PRIVATE).edit();
                    editor.putString("email", userEmail);
                    editor.putInt("id", userId);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, PersonalDetails.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("email",email);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void login(String email, String password) {
        this.email = emailEditText.getText().toString();
        this.password = passwordEditText.getText().toString();
        Map<String, String> params1 = new HashMap<>();
        params1.put("email", this.email);
        params1.put("password", this.password);
        JSONObject loginParams = new JSONObject(params1);
        //String loUrl = Constants.BASE_URL + Constants.LOGIN_URL;
        String loUrl ="http://139.59.65.145:9090/user/login";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, loUrl, loginParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject data = response.getJSONObject("data");
                    userId = data.getInt("id");

                        userEmail = data.getString("email");



                    Toast.makeText(LoginActivity.this, "Thanks for joining us.\nYour unique ID is: " + userId + ".\nPlease fill the details to continue", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREF, MODE_PRIVATE).edit();
                    editor.putString("email", userEmail);
                   // editor.putInt("userId", userId);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, Fragments.class);
                    intent.putExtra("userId", userId);

                    intent.putExtra("email",userEmail);
                    startActivity(intent);
                }

                 catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"failed to load"+e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"No such User exists",Toast.LENGTH_SHORT).show();
            }
        });
            queuelogin.add(request);
    }



    private void  setUpViews(){
        emailEditText=findViewById(R.id.emailText);
        passwordEditText=findViewById(R.id.password);
        loginButton=findViewById(R.id.login_button);
        signUpButton=findViewById(R.id.signup_button);
        queue= Volley.newRequestQueue(this);
        queuelogin= Volley.newRequestQueue(this);

    }

}
