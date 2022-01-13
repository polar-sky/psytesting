package info.fandroid.quizapp.quizapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.adapters.AttemptAdapter;
import info.fandroid.quizapp.quizapplication.json.Results;

public class AttemptActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String token;
    private int amountFinishedAttempts = 0;
    private List<Results> resultsList = new ArrayList<>();
    private TextView tvYourAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt);
        tvYourAttempts = findViewById(R.id.tvYourAttempts);
        Intent intent = getIntent();
        token = intent.getStringExtra("token_key");
        getResultsRequest();
    }

    public void getResultsRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/attempts";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArrayAttempts = response.getJSONArray("attempts");
                    for (int i = 0; i<jsonArrayAttempts.length();i++) {
                        JSONObject jsonObjectAttempts = jsonArrayAttempts.getJSONObject(i);
                        if ((jsonObjectAttempts.getBoolean("finished"))&&(jsonObjectAttempts.optJSONObject("result") != null)) {
                            amountFinishedAttempts++;
                            Results jsonResult = new Results();

                            jsonResult.setId_attempt(jsonObjectAttempts.getLong("id"));
                            jsonResult.setData_attempt(jsonObjectAttempts.getString("date"));
                            jsonResult.setAType(jsonObjectAttempts.getJSONObject("result").getString("type"));
                            Log.e("aaa", String.valueOf(amountFinishedAttempts));

                            addResultInResults(jsonResult);
                        }
                    }
                    if (amountFinishedAttempts == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.history_empty), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvYourAttempts.setText(error.getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.im_dead), Toast.LENGTH_SHORT);
                toast.show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map params = new HashMap();
                params.put("Authorization", "Bearer "+ token);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
    }


    private void addResultInResults(Results jsonResult) {
        resultsList.add(jsonResult);
        if (resultsList.size() == amountFinishedAttempts) {
            letsTry(resultsList);
        }
    }

    private void letsTry(List<Results> resultsList) {
        ListView ListViewAttemptsResultList = findViewById(R.id.ListViewAttemptsResultList);

        final AttemptAdapter adapter = new AttemptAdapter(this, R.layout.item_attempts, resultsList);
        ListViewAttemptsResultList.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}