package info.fandroid.quizapp.quizapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.json.Questions;
import info.fandroid.quizapp.quizapplication.json.Results;


public class StatisticActivity extends AppCompatActivity {

    HashMap<String, Integer> stats = new HashMap<>();
    Integer finished;
    Integer notfinished;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Bundle arguments = getIntent().getExtras();
        token = arguments.get("token_key").toString();

        getStatistics();

    }

    public void getStatistics() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/statistic";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    notfinished = response.getInt("notfinished");
                    finished = response.getInt("finished");
                    getPieChart(finished, notfinished);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", "Ошибка доступа к статистике" + error);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка доступа к статистике", Toast.LENGTH_SHORT);
                toast.show();
                goToMainActivity(token);
            }
        })  {
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
        queue.add(request);
    }

    private void getPieChart(Integer fin, Integer notfin){
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Оконченные", fin));
        data.add(new ValueDataEntry("Неоконченные", notfin));

        pie.setData(data);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }

    public void goToMainActivity(String string) {
        Intent main = new Intent(this, MainActivity.class);
        main.putExtra("token_key", string);
        startActivity(main);
        finish();
    }
}