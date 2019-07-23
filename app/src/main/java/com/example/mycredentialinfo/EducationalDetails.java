package com.example.mycredentialinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.squareup.picasso.Picasso;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.example.mycredentialinfo.Constants.BASE_URL;


public class EducationalDetails extends AppCompatActivity {

    final private String imageUri = "content://media/internal/images/media";
    final private String imageUrl = "http://139.59.65.145:9090/user/educationdetail/certificate/";

    private Button saveButton;
    private EditText organizationEditText, degreeEditText, locationEditText;
    private Spinner startYearSpinner, endYearSpinner;
    private ImageView certiPic;

    private String organization, degree, location;
    private String startYear, endYear;
    //private ImageView Certificate;
    private final int IMAGE_REQUEST = 1;


    private static String url = "http://139.59.65.145:9090//user/educationdetail/certificate/";
    private Bitmap bitmap;

    private int userId;

    private String picturePath;

    private Bitmap certiPicBitmap;
    private ByteArrayOutputStream bos;
    private byte[] imageByteArray;
    private String encodedImage;
    RequestQueue queue1, queue2, queue3, queue4,queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_details);
        setUpView();
        certiPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(imageUri));
                startActivityForResult(intent, 1);
            }
        });

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREF, MODE_PRIVATE);
        //userId = prefs.getInt("id", 0);
        Intent intent= getIntent();
        userId = intent.getIntExtra("userId", 0);
        Toast.makeText(EducationalDetails.this,""+userId,Toast.LENGTH_SHORT).show();

        final String isUpdate = getIntent().getStringExtra("update");

        if (isUpdate == null)
            getSupportActionBar().setTitle("Set Education Details");
        else {
            getSupportActionBar().setTitle("Edit Education Details");
            getEducationDetails();
           // getCertiImg();
        }

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                organization = organizationEditText.getText().toString().trim();
                degree = degreeEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                startYear = startYearSpinner.getSelectedItem().toString();
                endYear = endYearSpinner.getSelectedItem().toString();

                //EduDetails educationDetails = new EduDetails(startYear, degree, organization, location, endYear);

                if (isUpdate == null)
                    setEducationDetails(startYear, degree, organization, location, endYear);
                else {
                    updateEducationDetails(startYear, degree, organization, location, endYear);
                }
               // UploadIMG();
            }
        });
    }

    private void setCertiImg() {





        File file = new File(picturePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo",file.getName(),requestBody);
        RequestBody userid = RequestBody.create(MultipartBody.FORM, String.valueOf(userId));
        Toast.makeText(EducationalDetails.this,userid.toString()+" "+part.toString(),Toast.LENGTH_SHORT).show();
        Map<String,String> map= new HashMap<>();
        map.put("photo",part.toString());
        map.put("uid",userId+"");


        JSONObject getPerParams = new JSONObject(map);
        String setPerUrl = Constants.BASE_URL + Constants.SET_PROFILE_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,setPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                    Toast.makeText(EducationalDetails.this, " Certi Image Upload successful", Toast.LENGTH_SHORT).show();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EducationalDetails.this, "Certificate upload Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue3.add(request);



    }


    private void getCertiImg() {
        Uri uri = Uri.parse(imageUrl + userId);
        Picasso.with(getApplicationContext()).load(uri).into(certiPic);
    }

    private void setEducationDetails(String startYear, String degree, String organization, String location, String endYear) {

        Map<String, String> params = new HashMap<>();


        params.put("start_year",startYear);
        params.put("degree", degree);
        params.put("organisation", organization);
        params.put("location", location);
        params.put("end_year", endYear);
        JSONObject getPerParams = new JSONObject(params);
        String setEduUrl = BASE_URL + Constants.SET_EDU_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,setEduUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {




                    JSONObject data = response.getJSONObject("data");

                /*    EduData eduData = new EduData();
                    eduData.setDegree(data.getString("degree"));
                    eduData.setOrganisation(data.getString("organisation"));
                    eduData.setLocation(data.getString("location"));
                    eduData.setEnd_year(data.getString("end_year"));
                    eduData.setStart_year(data.getString("start_year"));



                   EduDetailsData eduDetailsData = new  EduDetailsData();
                   eduDetailsData.setData(eduData);*/
                    Intent intent = new Intent(EducationalDetails.this, Fragments.class);
                    intent.putExtra("userId", userId);
                    finish();
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EducationalDetails.this, "Failed to load  ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EducationalDetails.this, "Set Educational details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }


    private void updateEducationDetails(String startYear, String degree, String organization, String location, String endYear) {
        Map<String, String> params2 = new HashMap<>();


        params2.put("start_year",startYear);
        params2.put("degree", degree);
        params2.put("organisation", organization);
        params2.put("location", location);
        params2.put("end_year", endYear);
        JSONObject getPerParams = new JSONObject(params2);
        String setPerUrl = BASE_URL + Constants.SET_EDU_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,setPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject data = response.getJSONObject("data");
                    /*EduData eduData = new EduData();
                    eduData.setDegree(data.getString("degree"));
                    eduData.setOrganisation(data.getString("organisation"));
                    eduData.setLocation(data.getString("location"));
                    eduData.setEnd_year(data.getString("end_year"));
                    eduData.setStart_year(data.getString("start_year"));



                    EduDetailsData eduDetailsData = new  EduDetailsData();
                    eduDetailsData.setData(eduData);*/

                    Toast.makeText(EducationalDetails.this, "Educational Details Updated", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(EducationalDetails.this, Fragments.class);
                    intent1.putExtra("id", userId);
                    finish();
                    startActivity(intent1);

                } catch (JSONException e) {
                    Toast.makeText(EducationalDetails.this, "Failed to Load", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EducationalDetails.this, "Update Educational details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }


    private void getEducationDetails() {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = BASE_URL + Constants.GET_EDU_URL + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");



                        organizationEditText.setText(data.getString("organisation"));
                        degreeEditText.setText(data.getString("degree"));
                        locationEditText.setText(data.getString("location"));
                        startYearSpinner.setSelection(getIndex(startYearSpinner, data.getString("start_year")));
                        endYearSpinner.setSelection(getIndex(endYearSpinner, data.getString("end_year")));


                } catch (JSONException e) {
                    Toast.makeText(EducationalDetails.this, "Failed to Load", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EducationalDetails.this, "Response Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }

    private void setUpView() {
        organizationEditText = findViewById(R.id.organization_edit_text);
        degreeEditText = findViewById(R.id.degree_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        startYearSpinner = findViewById(R.id.start_year_spinner);
        endYearSpinner = findViewById(R.id.end_year_spinner);

        certiPic = findViewById(R.id.certi);
        queue1 = Volley.newRequestQueue(this);
        queue2 = Volley.newRequestQueue(this);
        queue3 = Volley.newRequestQueue(this);
        queue4 = Volley.newRequestQueue(this);
        queue = Volley.newRequestQueue(this);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private String getRealPathFromURIPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();
            certiPic.setImageURI(uri);

            picturePath = getRealPathFromURIPath(uri);
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                certiPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private byte[] getFileDataFromDrawable(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void UploadIMG(){
        VolleyMutilpartRequest volleyMultipartRequest = new VolleyMutilpartRequest (url, null, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try{
                    JSONObject obj = new JSONObject(new String(response.data));
                    if (obj.toString().contains("Success")){
                        Toast.makeText(getApplicationContext(),"Image uploaded Successfully",Toast.LENGTH_SHORT).show();
                       // OpenProfessionaldetails();
                    }else{
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String id = getIntent().getExtras().getString("userId");
                params.put("uid",id);
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
                params.put("photo",new DataPart(imageName +".jpg",getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        queue.add(volleyMultipartRequest);
    }
}
