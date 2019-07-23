package com.example.mycredentialinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;



public class PersonalDetails extends AppCompatActivity {

    final private String imageUri = "content://media/internal/images/media";
    final private String imageUrl = "http://139.59.65.145:9090/user/personaldetail/pp/post/";

    private String name, email, mobile, location, links, skills;
    private int userId;

    private EditText nameEditText, emailEditText, mobileEditText, locationEditText, linksEditText, skillsEditText;
    private ImageView ProfilePic;
    private Button saveButton;

    //UserService userService;

    private Bitmap profilePicBitmap;
    private ByteArrayOutputStream bos;
    private byte[] imageByteArray;
    private String encodedImage;
    RequestQueue queue1,queue2,queue3,queue4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        setUp();
        Intent intent= getIntent();
        userId = intent.getIntExtra("userId", 0);
        email=intent.getStringExtra("email");
        Toast.makeText(PersonalDetails.this,""+userId+" "+email,Toast.LENGTH_SHORT).show();
        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(imageUri));
                startActivityForResult(intent, 1);
            }
        });

        //userService = APIUtils.getUserService();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREF, MODE_PRIVATE);
       // userId = prefs.getInt("id", 0);
       // email= prefs.getString("email",null);

        Toast.makeText(PersonalDetails.this,"userId:"+userId+" email:"+email,Toast.LENGTH_SHORT).show();
        final String isUpdate = getIntent().getStringExtra("update");

        if(isUpdate == null)
            getSupportActionBar().setTitle("Set Personal Details");

        else {
            getSupportActionBar().setTitle("Edit Personal Details");
           getPersonalDetails();
           getProfileImg();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEditText.getText().toString().trim();
                mobile = mobileEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                links = linksEditText.getText().toString().trim();
                skills = skillsEditText.getText().toString().trim();
               // Toast.makeText(PersonalDetails.this," "+name+" "+email+" "+skills+" "+mobile+" "+location+" "+links,Toast.LENGTH_LONG).show();
                //PerDetails personalDetails = new PerDetails(skills, mobile, name, links, location, email);

               if (isUpdate == null){
                  // Toast.makeText(PersonalDetails.this,"Set this",Toast.LENGTH_SHORT).show();
                   setPersonalDetails(email,name,mobile,location,links,skills);
                   Toast.makeText(PersonalDetails.this,"All set",Toast.LENGTH_SHORT).show();
               }
                else {
                    updatePersonalDetails(email,name,mobile,location,links,skills);

                }
                setProfileImg(encodedImage);

            };

        });
    }



    private void getPersonalDetails() {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PER_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");

                        nameEditText.setText(data.getString("name"));
                        mobileEditText.setText(data.getString("mobile_no"));
                        locationEditText.setText(data.getString("location"));
                        linksEditText.setText(data.getString("links"));
                        skillsEditText.setText(data.getString("skills"));


                } catch (JSONException e) {
                    Toast.makeText(PersonalDetails.this, "Failed to Load: " , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalDetails.this, "Response Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }


    private void updatePersonalDetails(final String email, String name, String mobile, String location, String links, String skills) {

        Map<String, String> params2 = new HashMap<>();

        params2.put("email", email);
        params2.put("name", name);
        params2.put("links", links);
        params2.put("location", location);
        params2.put("mobile_no", mobile);
        params2.put("skills", skills);
        JSONObject getPerParams = new JSONObject(params2);
        String setPerUrl = Constants.BASE_URL + Constants.SET_PER_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,setPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject data = response.getJSONObject("data");

                  /*  PerData perData1 = new PerData();
                    perData1.setEmail(email);
                    perData1.setName(data.getString("name"));
                    perData1.setId(data.getString("id"));
                    perData1.setImage(data.getString("image"));
                    perData1.setLinks(data.getString("links"));
                    perData1.setMobile_no(data.getString("mobile_no"));
                    perData1.setLocation(data.getString("location"));
                    perData1.setUid(userId+"");
                    perData1.setSkills(data.getString("skills"));


                    PerDetailsData personalDetailsData1 = new PerDetailsData();
                    personalDetailsData1.setData(perData1);*/
                    Toast.makeText(PersonalDetails.this, "Personal Details Updated", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(PersonalDetails.this, Fragments.class);
                    intent1.putExtra("userId", userId);
                    finish();
                    startActivity(intent1);

                } catch (JSONException e) {
                    Toast.makeText(PersonalDetails.this, "Failed to Load: " , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalDetails.this, "Update Personal details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }


    private void setPersonalDetails(String email,String name,String mobile,String location,String links,String skills) {
       // Toast.makeText(PersonalDetails.this," "+name+" "+email+" "+skills+" "+mobile+" "+location+" "+links,Toast.LENGTH_LONG).show();
        Map<String, String> params = new HashMap<>();


        params.put("skills",skills);
        params.put("mobile_no", mobile);
        params.put("name", name);
        params.put("links", links);
        params.put("location", location);
        params.put("email", email);

        JSONObject setPerParams = new JSONObject(params);
        String setPerUrl = Constants.BASE_URL + Constants.SET_PER_URL + userId +"";

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.POST,setPerUrl, setPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject data = response.getJSONObject("data");

                  /*  PerData perData = new PerData();
                    perData.setEmail(data.getString("email"));
                    perData.setName(data.getString("name"));
                    perData.setId(data.getString("id"));
                    perData.setImage(data.getString("image"));
                    perData.setLinks(data.getString("links"));
                    perData.setMobile_no(data.getString("mobile_no"));
                    perData.setLocation(data.getString("location"));
                    perData.setUid(data.getString("uid"));
                    perData.setSkills(data.getString("skills"));

                    Toast.makeText(PersonalDetails.this," "+perData.toString(),Toast.LENGTH_LONG).show();
                    PerDetailsData personalDetailsData = new PerDetailsData();
                    personalDetailsData.setData(perData);*/
                    Intent intent = new Intent(PersonalDetails.this, ProfessionalDetails.class);
                    intent.putExtra("userId", userId);
                    finish();
                    startActivity(intent);

                } catch (JSONException e) {
                    Toast.makeText(PersonalDetails.this,"catch",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Toast.makeText(PersonalDetails.this, "Failed to Load: " , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalDetails.this, "Set Personal details Failed: ", Toast.LENGTH_SHORT).show();
            }
        });
        queue3.add(request3);

    }

    private void setProfileImg(String encodedImage) {
        Photo photo = new Photo(String.valueOf(userId),encodedImage);


        Map<String, String> params = new HashMap<>();

        params.put("photo", encodedImage);
        params.put("uid", userId+"");
        JSONObject getPerParams = new JSONObject(params);
        String setPerUrl = Constants.BASE_URL + Constants.SET_PROFILE_URL;
        JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.POST,setPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(PersonalDetails.this, "Profile pic set", Toast.LENGTH_SHORT).show();
                /*try {
                    JSONObject status_message = response.getJSONObject("status_message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalDetails.this, "Profile pic upload Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue4.add(request4);

    }
    private void getProfileImg() {
        Uri uri = Uri.parse(imageUrl + userId);
        Picasso.with(getApplicationContext()).load(uri).into(ProfilePic);
    }


    private void setUp() {
        nameEditText = findViewById(R.id.full_name_edit_text);
        mobileEditText = findViewById(R.id.mobile_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        linksEditText = findViewById(R.id.links_edit_text);
        skillsEditText = findViewById(R.id.skills_edit_text);
        saveButton = findViewById(R.id.save_button);

        ProfilePic = findViewById(R.id.user_profile_pic);
        queue1= Volley.newRequestQueue(this);
        queue2= Volley.newRequestQueue(this);
        queue3= Volley.newRequestQueue(this);
        queue4= Volley.newRequestQueue(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1)
        {
            Uri uri = data.getData();
            ProfilePic.setImageURI(uri);

            //String picturePath = getRealPathFromURIPath(uri, PersonalDetailsActivity.this);

            profilePicBitmap = ((BitmapDrawable) ProfilePic.getDrawable()).getBitmap();
            //BitmapFactory.decodeFile(picturePath);
            bos = new ByteArrayOutputStream();
            profilePicBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            imageByteArray = bos.toByteArray();
            encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        }
    }
}