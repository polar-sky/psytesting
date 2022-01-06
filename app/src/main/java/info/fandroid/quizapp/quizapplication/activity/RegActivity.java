package info.fandroid.quizapp.quizapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.fandroid.quizapp.quizapplication.R;

public class RegActivity extends AppCompatActivity {

    private Button register;
    private EditText username;
    private EditText email;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view){
                regRequest(view);
                Context context = RegActivity.this;
                Class destinationActivity = LoginActivity.class;
                Intent mainActivityIntent = new Intent(context, destinationActivity);
                startActivity(mainActivityIntent);
            }
        });

    }

    public void regRequest(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL =  "https://psytest-mbti.herokuapp.com/api/auth/signup";
        Map params = new HashMap();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("username", username.getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Вы зарегистрированы", Toast.LENGTH_SHORT);
                        goToLoginActivity();
                        toast.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(request);
    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}