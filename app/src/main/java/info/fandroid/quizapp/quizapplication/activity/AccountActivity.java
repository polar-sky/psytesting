package info.fandroid.quizapp.quizapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import info.fandroid.quizapp.quizapplication.R;

public class AccountActivity extends AppCompatActivity {

    private String token;
    private String isAuth;

    private TextView tvUserName;
    private TextView tvEmail;
    private Button btnAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();

        token = intent.getStringExtra("token_key");
        isAuth = intent.getStringExtra("isAuth");

        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        btnAttempts = findViewById(R.id.btnAttempts);

        getAccountRequest();

        View.OnClickListener btnAttemptsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAttemptActivity();
            }
        };

        btnAttempts.setOnClickListener(btnAttemptsClick);
    }

    private void goToAttemptActivity() {
        Intent i = new Intent(this, AttemptActivity.class);
        i.putExtra("token_key", token);
        startActivity(i);
    }

    private void getAccountRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/details";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!response.equals(null)) {
                    try {
                        tvUserName.setText(response.getString("username"));
                        tvEmail.setText(response.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
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
        requestQueue.add(request);
    }
}