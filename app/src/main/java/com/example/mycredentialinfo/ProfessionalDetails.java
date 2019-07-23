package com.example.mycredentialinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfessionalDetails extends AppCompatActivity {
    Button saveButton;
    EditText organizationEditText, designationEditText;
    Spinner startMonthSpinner, startYearSpinner, endMonthSpinner, endYearSpinner;
    CheckBox currWorkCheckBox;
    LinearLayout endLinearLayout;
    String organization, designation;
    String startMonth, startYear, endMonth, endYear;
    String startDate, endDate;
    RequestQueue queue1, queue2, queue3, queue4;


    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_details);
        setUpViews();
        currWorkCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currWorkCheckBox.isChecked())
                {
                    endLinearLayout.setVisibility(View.GONE);
                }
                else if(!currWorkCheckBox.isChecked())
                {
                    endLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });



        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREF, MODE_PRIVATE);
        //userId = prefs.getInt("userId", 0);
        Intent intent= getIntent();
        userId = intent.getIntExtra("userId", 0);
        Toast.makeText(ProfessionalDetails.this,""+userId,Toast.LENGTH_SHORT).show();
        //email=intent.getStringExtra("email");

        final String isUpdate = getIntent().getStringExtra("update");

        if (isUpdate == null)
            getSupportActionBar().setTitle("Set Professional Details");
        else {
            getSupportActionBar().setTitle("Edit Professional Details");
            getProfessionalDetails();
        }

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organization = organizationEditText.getText().toString().trim();
                designation = designationEditText.getText().toString().trim();
                startMonth = startMonthSpinner.getSelectedItem().toString();
                startYear = startYearSpinner.getSelectedItem().toString();
                if(currWorkCheckBox.isChecked())
                {
                    endMonth = endMonthSpinner.getItemAtPosition(Integer.parseInt(getDate().substring(3, 5)) - 1).toString();
                    endYear = getDate().substring(6);
                }
                else if(!currWorkCheckBox.isChecked())
                {
                    endMonth = endMonthSpinner.getSelectedItem().toString();
                    endYear = endYearSpinner.getSelectedItem().toString();
                }

                startDate = startMonth + ", " + startYear;
                endDate = endMonth + ", " + endYear;

                ProfDetails professionalDetails = new ProfDetails(endDate, organization, designation, startDate);

                if (isUpdate == null)
                    setProfessionalDetails(professionalDetails);
                else {
                    updateProfessionalDetails(professionalDetails);
                }
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private String getDate(){
        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        String date = dfDate.format(Calendar.getInstance().getTime());
        return date;
    }

    private void updateProfessionalDetails(ProfDetails professionalDetails) {

        Map<String, String> params2 = new HashMap<>();

        params2.put("start_date",startDate);
        params2.put("designation", designation);
        params2.put("organisation", organization);

        params2.put("end_date", endDate);
        JSONObject getPerParams = new JSONObject(params2);
        String setPerUrl = Constants.BASE_URL + Constants.SET_PROF_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,setPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject data = response.getJSONObject("data");

                    ProfData profData = new ProfData();
                    profData.setEnd_date(data.getString("end_date"));
                    profData.setOrganisation(data.getString("organisation"));
                    profData.setDesignation(data.getString("designation"));
                    profData.setStart_date(data.getString("start_date"));



                    ProfDetailsData profDetailsData = new ProfDetailsData();
                    profDetailsData.setData(profData);
                    Toast.makeText(ProfessionalDetails.this, "Professional Details Updated", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(ProfessionalDetails.this, Fragments.class);
                    intent1.putExtra("userId", userId);
                    finish();
                    startActivity(intent1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfessionalDetails.this, "Update Educational details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }


    private void setProfessionalDetails(ProfDetails professionalDetails) {
        Map<String, String> params = new HashMap<>();

        params.put("start_date",startDate);
        params.put("designation", designation);
        params.put("organisation", organization);

        params.put("end_date", endDate);
        JSONObject getPerParams = new JSONObject(params);
        String setEduUrl = Constants.BASE_URL + Constants.SET_PROF_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,setEduUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject data = response.getJSONObject("data");

                    ProfData profData = new ProfData();
                    profData.setEnd_date(data.getString("end_date"));
                    profData.setOrganisation(data.getString("organisation"));
                    profData.setDesignation(data.getString("designation"));
                    profData.setStart_date(data.getString("start_date"));



                   ProfDetailsData profDetailsData = new ProfDetailsData();
                    profDetailsData.setData(profData);
                    Intent intent = new Intent(ProfessionalDetails.this, EducationalDetails.class);
                    intent.putExtra("userId", userId);
                    finish();
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfessionalDetails.this, "Set Professional details Failed: ", Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);

    }

    private void getProfessionalDetails() {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PROF_URL+userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");

                       // nameEditText.setText(data.getString("name"));
                        organizationEditText.setText(data.getString("organisation"));
                        designationEditText.setText(data.getString("designation"));
                        startMonthSpinner.setSelection(getIndex(startMonthSpinner, data.getString("start_date").substring(0, 3)));
                        startYearSpinner.setSelection(getIndex(startYearSpinner, data.getString("start_date").substring(5)));
                        endMonthSpinner.setSelection(getIndex(endMonthSpinner, data.getString("end_date").substring(0, 3)));
                        endYearSpinner.setSelection(getIndex(endYearSpinner, data.getString("end_date").substring(5)));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfessionalDetails.this, "Response Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }


    private void setUpViews(){
        organizationEditText = findViewById(R.id.organization_edit_text);
        designationEditText = findViewById(R.id.designation_edit_text);
        startMonthSpinner = findViewById(R.id.start_month_spinner);
        startYearSpinner = findViewById(R.id.start_year_spinner);
        endMonthSpinner = findViewById(R.id.end_month_spinner);
        endYearSpinner = findViewById(R.id.end_year_spinner);
        endLinearLayout = findViewById(R.id.end_date_linear_layout);

        currWorkCheckBox = findViewById(R.id.curr_work_check_box);
        queue1 = Volley.newRequestQueue(this);
        queue2 = Volley.newRequestQueue(this);
        queue3 = Volley.newRequestQueue(this);
        queue4 = Volley.newRequestQueue(this);
    }
}
