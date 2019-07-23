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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class ProfessionalFragment extends Fragment {

    private TextView organizationTextView, designationTextView, startDateTextView, endDateTextView;
    private Button updateDetailsButton;
    RequestQueue queue1;
    private int userId;

    public ProfessionalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_professional_fragment, container, false);
        organizationTextView = rootView.findViewById(R.id.organization_text_view);
        designationTextView = rootView.findViewById(R.id.designation_text_view);
        startDateTextView = rootView.findViewById(R.id.start_date_text_view);
        endDateTextView = rootView.findViewById(R.id.end_date_text_view);
         queue1= Volley.newRequestQueue(getContext());
        updateDetailsButton = rootView.findViewById(R.id.update_details_button);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREF, MODE_PRIVATE);
        //userId = prefs.getInt("id", 0);
        userId = Fragments.intent5.getIntExtra("userId", 0);
        Toast.makeText(getContext(),""+userId,Toast.LENGTH_SHORT).show();
        getProfessionalDetails(userId);

        updateDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfessionalDetails.class);
                intent.putExtra("update", "true");
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void getProfessionalDetails(int userId) {
        JSONObject getPerParams = new JSONObject();
        String getPerUrl = Constants.BASE_URL + Constants.GET_PROF_URL+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,getPerUrl, getPerParams, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");


                        organizationTextView.setText(data.getString("organisation"));
                        designationTextView.setText(data.getString("designation"));
                        startDateTextView.setText(data.getString("start_date"));
                        endDateTextView.setText( data.getString("end_date"));


                } catch (JSONException e) {
                    Toast.makeText(getContext(), " professional details:null " , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get professional details failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(request);

    }
}

