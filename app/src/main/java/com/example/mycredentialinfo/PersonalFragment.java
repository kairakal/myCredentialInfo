package com.example.mycredentialinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment extends Fragment {

    private TextView nameTextView, mobileTextView, locationTextView, linksTextView, skillsTextView;
    private Button updateDetailsButton;

    private int userId;
    RequestQueue queue1;
   // private ImageView ProfilePic;

    public PersonalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_personal_fragment, container, false);



        nameTextView = rootView.findViewById(R.id.name_text_view);
        mobileTextView = rootView.findViewById(R.id.mobile_text_view);
        locationTextView = rootView.findViewById(R.id.location_text_view);
        linksTextView = rootView.findViewById(R.id.links_text_view);
        skillsTextView = rootView.findViewById(R.id.skills_text_view);
        queue1 = Volley.newRequestQueue(getContext());
        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREF, MODE_PRIVATE);
        //userId = prefs.getInt("id", 0);
        userId = Fragments.intent5.getIntExtra("userId", 0);
        Toast.makeText(getContext(),""+userId,Toast.LENGTH_SHORT).show();
        getPersonalDetails(userId);

        updateDetailsButton = rootView.findViewById(R.id.update_details_button);

        updateDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PersonalDetails.class);
                intent.putExtra("update", "true");
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void getPersonalDetails(final int userId)
    {

        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PER_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");

                        nameTextView.setText(data.getString("name"));
                        mobileTextView.setText(data.getString("mobile_no"));
                        locationTextView.setText(data.getString("location"));
                        linksTextView.setText(data.getString("links"));
                        skillsTextView.setText(data.getString("skills"));


                } catch (JSONException e) {
                    Toast.makeText(getContext(), "data personal details:null", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Fetch Personal Details Failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);
    }
}
