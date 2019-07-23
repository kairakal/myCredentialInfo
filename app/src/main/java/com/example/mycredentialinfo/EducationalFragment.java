package com.example.mycredentialinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mycredentialinfo.Constants.BASE_URL;
import static com.example.mycredentialinfo.LoginActivity.MY_PREF;

public class EducationalFragment extends Fragment {

    final private String imageUrl = "http://139.59.65.145:9090/user/educationdetail/certificate/";

    private TextView organizationTextView, degreeTextView, locationTextView, startYearTextView, endYearTextView;
    private Button updateDetails;
    private ImageView certiPic;

    private int userId;
    RequestQueue queue1,queue2;
   // UserService userService;

    public EducationalFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.activity_educational_fragment, container, false);
       organizationTextView = rootView.findViewById(R.id.organization_text_view);
        degreeTextView = rootView.findViewById(R.id.degree_text_view);
        locationTextView = rootView.findViewById(R.id.location_text_view);
        startYearTextView = rootView.findViewById(R.id.start_year_text_view);
        endYearTextView = rootView.findViewById(R.id.end_year_text_view);
        certiPic = rootView.findViewById(R.id.certi);
        queue1 = Volley.newRequestQueue(getContext());
        queue2 = Volley.newRequestQueue(getContext());

        updateDetails = rootView.findViewById(R.id.update_details_button1);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREF, MODE_PRIVATE);
       // userId = prefs.getInt("userId", 0);

        userId = Fragments.intent5.getIntExtra("userId", 0);
        Toast.makeText(getContext(),""+userId,Toast.LENGTH_SHORT).show();
        //email=Fragments.intent5..getStringExtra("email","");

        getEducationDetails(userId);
      //  getCertiPic();

       updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // updateEducation(startYearTextView.getText().toString(), degreeTextView.getText().toString(), organizationTextView.getText().toString(), locationTextView.getText().toString(), endYearTextView.getText().toString());
                Intent intent = new Intent(getContext(), EducationalDetails.class);
                intent.putExtra("update", "true");
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateEducation(String startYear, String degree, String organization, String location, String endYear) {
        Map<String, String> params2 = new HashMap<>();


        params2.put("start_year",startYear);
        params2.put("degree", degree);
        params2.put("organisation", organization);
        params2.put("location", location);
        params2.put("end_year", endYear);
        JSONObject getPerParams = new JSONObject(params2);
        String setPerUrl = BASE_URL + Constants.SET_EDU_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,setPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
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



                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Failed to Load", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Update Educational details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(request);
    }

    private void getCertiPic() {
        Uri uri = Uri.parse(imageUrl + userId);
        Picasso.with(getActivity()).load(uri).into(certiPic);
    }

    private void getEducationDetails(int userId) {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_EDU_URL + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");



                        organizationTextView.setText(data.getString("organisation"));
                        degreeTextView.setText(data.getString("degree"));
                        locationTextView.setText(data.getString("location"));
                        startYearTextView.setText( data.getString("start_year"));
                        endYearTextView.setText( data.getString("end_year"));


                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Education Details:null ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get education details filed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);


    }

}
