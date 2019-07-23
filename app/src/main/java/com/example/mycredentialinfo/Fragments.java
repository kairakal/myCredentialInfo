package com.example.mycredentialinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mycredentialinfo.LoginActivity.MY_PREF;

public class Fragments extends AppCompatActivity {
    final private String imageUrl = "http://139.59.65.145:9090/user/personaldetail/profilepic/";

    private TextView nameTextView, emailTextView, organizationTextView, locationTextView;

    private ImageView ProfilePic;

    private String name, email, organization, location;

    private ViewPager mViewPager;

    private int userId;
    public  static Intent intent5;
       RequestQueue queue1,queue2,queue3,queue4;
    private Bitmap profilePicBitmap;
    private ByteArrayOutputStream bos;
    private byte[] imageByteArray;
    private String encodedImage;
    //private UserService userService;

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        public SimpleFragmentPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0 :
                    return new EducationalFragment();
                case 1 :
                    return new ProfessionalFragment();
                case 2:
                    return new PersonalFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Education";
                case 1:
                    return "Professional";
                case 2:
                    return "Personal";
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        nameTextView = findViewById(R.id.name_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        organizationTextView = findViewById(R.id.organization_text_view);
        locationTextView = findViewById(R.id.location_text_view);
         queue1= Volley.newRequestQueue(this);
         queue2= Volley.newRequestQueue(this);
        queue3= Volley.newRequestQueue(this);
        queue4= Volley.newRequestQueue(this);
        ProfilePic = findViewById(R.id.user_profile_pic);

        name = email = organization = location = "";
        intent5= getIntent();
        userId = intent5.getIntExtra("userId", 0);
        Toast.makeText(Fragments.this,""+userId,Toast.LENGTH_SHORT).show();
       // email=intent5.getStringExtra("email");
        SharedPreferences prefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);

       email = prefs.getString("email", "-");
       // userId = prefs.getInt("id", 0);

        emailTextView.setText(email);

        SimpleFragmentPagerAdapter simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.fragment_container);
        mViewPager.setAdapter(simpleFragmentPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        getProfilePic();
        getPersonalDetails();
       //getEducationalDetails();
        getProfessionalDetails();
    }



    private void getProfessionalDetails() {
        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PROF_URL+userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    if(data != null) {
                        organizationTextView.setText(data.getString("organisation"));

                    } else {
                        Toast.makeText(Fragments.this, "Professional Details Response Empty", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Fragments.this, "Response Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }


    private void getPersonalDetails() {
        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PER_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    if(data != null) {
                        nameTextView.setText(data.getString("name"));
                        //mobi.setText(data.getString("mobile_no"));
                        locationTextView.setText(data.getString("location"));
                        //linksT.setText(data.getString("links"));
                        //skillsEditText.setText(data.getString("skills"));
                    } else {
                        Toast.makeText(Fragments.this, "Personal Details Empty", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Fragments.this, "Response Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }
    private void getProfilePic() {
        Uri uri = Uri.parse(imageUrl + userId);
        Picasso.with(getApplicationContext()).load(uri).into(ProfilePic);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sign_out_menu:
                SharedPreferences prefs = getSharedPreferences(MY_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(this, "Signed out Successfully!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Fragments.this, LoginActivity.class));
                return true;
            case R.id.delete_account_menu:
                deleteAccount();
                finish();
                Intent intent = new Intent(Fragments.this, LoginActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAccount() {
        deleteProfessionalData();
        deleteEducationData();
        Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Please login or signup to proceed!", Toast.LENGTH_SHORT).show();
    }

    private void deleteProfessionalData() {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PROF_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Fragments.this,"Professional Details Deleted",Toast.LENGTH_LONG).show();
            /*    try {
                    JSONObject status_message = response.getJSONObject("status_message");

                    }

                 catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Fragments.this, "Delete Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue3.add(request);
    }

    public void deleteEducationData()
    {JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_EDU_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // try {
                   // JSONObject status_message = response.getJSONObject("status_message");
                    Toast.makeText(Fragments.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                //}

               // catch (JSONException e) {
                //    e.printStackTrace();
              //  }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Fragments.this, "Delete Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue4.add(request);
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


