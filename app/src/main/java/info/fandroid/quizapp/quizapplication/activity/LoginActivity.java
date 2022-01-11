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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import info.fandroid.quizapp.quizapplication.R;

public class LoginActivity extends AppCompatActivity {

    public EditText editTextNickname;
    public EditText editTextPassword;
    public Button btnAuthSubmit;
    private Button register;

    public String token = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_loginn);
        super.onCreate(savedInstanceState);
        btnAuthSubmit = (Button) findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = LoginActivity.this;
                Class destinationActivity = RegActivity.class;
                Intent mainActivityIntent = new Intent(context, destinationActivity);
                startActivity(mainActivityIntent);

            }
        });

        btnAuthSubmit.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            btnAuth(view);

        }
    });

    }

    public void btnAuth(View view) {
        editTextNickname = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        String username = editTextNickname.getText().toString();
        String password = editTextPassword.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = getResources().getString(R.string.URL) + "/api/auth/signin";
        HashMap<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("username", username);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            token = response.getString("accessToken");
                            Log.d("Ключ", token);
                            goToMainActivity(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Вход выполнен", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", "Unable to login" + error);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вход не выполнен", Toast.LENGTH_SHORT);
                toast.show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


    public void goToMainActivity(String string) {
        Intent main = new Intent(this, MainActivity.class);
        main.putExtra("token_key", string);
        startActivity(main);
        finish();
    }

    public void goToLoginActivity() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}
