package info.fandroid.quizapp.quizapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.JsonObjectRequest;

import info.fandroid.quizapp.quizapplication.R;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginn);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = LoginActivity.this;
                Class destinationActivity = MainActivity.class;
                Intent mainActivityIntent = new Intent(context, destinationActivity);
                startActivity(mainActivityIntent);

            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = LoginActivity.this;
                Class destinationActivity = RegActivity.class;
                Intent mainActivityIntent = new Intent(context, destinationActivity);
                startActivity(mainActivityIntent);

            }
        });

        /*JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            token = response.getString("accessToken");
                            Log.d("Ключ", token);
                            startActivity(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.auth_fail), Toast.LENGTH_SHORT);
                toast.show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        requestQueue.add(request_json);
    }*/


}
}
