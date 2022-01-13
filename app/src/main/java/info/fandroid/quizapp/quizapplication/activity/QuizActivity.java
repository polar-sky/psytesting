package info.fandroid.quizapp.quizapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import info.fandroid.quizapp.quizapplication.json.Questions;
import info.fandroid.quizapp.quizapplication.json.Results;


public class QuizActivity extends BaseActivity {

    private int JP = 0;
    private int SN = 0;
    private int EI = 0;
    private int TF = 0;

    private TextView tvQuestionText;
    private TextView tvQuestionTitle;
    private Button answerBtn5;
    private Button answerBtn4;
    private Button answerBtn3;
    private Button answerBtn2;
    private Button answerBtn1;
    private String token;
    private List<Questions> questionList = new ArrayList<>();
    private Questions currentQuestion = new Questions();
    private boolean running;
    private int seconds;
    private String isAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Bundle arguments = getIntent().getExtras();
        token = arguments.get("token_key").toString();
        Log.e("ТОКЕН2", token + "");
        //isAuth = arguments.get("isAuth").toString();

        runTimer();
        running = true;
        tvQuestionText = (TextView) findViewById(R.id.tvQuestionText);
        tvQuestionTitle = (TextView) findViewById(R.id.tvQuestionTitle);
        answerBtn1 = (Button) findViewById(R.id.answerBtn1);
        answerBtn2 = (Button) findViewById(R.id.answerBtn2);
        answerBtn3 = (Button) findViewById(R.id.answerBtn3);
        answerBtn4 = (Button) findViewById(R.id.answerBtn4);
        answerBtn5 = (Button) findViewById(R.id.answerBtn5);
        getQuestion();
        View.OnClickListener ButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.answerBtn1:
                        answer(1);
                        break;
                    case R.id.answerBtn2:
                        answer(2);
                        break;
                    case R.id.answerBtn3:
                        answer(3);
                        break;
                    case R.id.answerBtn4:
                        answer(4);
                        break;
                    case R.id.answerBtn5:
                        answer(5);
                        break;
                }
            }
        };
        answerBtn1.setOnClickListener(ButtonClickListener);
        answerBtn2.setOnClickListener(ButtonClickListener);
        answerBtn3.setOnClickListener(ButtonClickListener);
        answerBtn4.setOnClickListener(ButtonClickListener);
        answerBtn5.setOnClickListener(ButtonClickListener);
    }


    public void getQuestion() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/questions";
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        //JSONArray jsonArray = response.getJSONArray(i);

                        Questions questions = new Questions();
                        JSONObject jsonObject = response.getJSONObject(i);

                        String question = jsonObject.getString("text1");

                        questions.setQuestionText(jsonObject.getString(
                                "text1"
                        ));
                        questions.setType(jsonObject.getString("type"));
                        questions.setId(jsonObject.getInt("id"));
                        addQuestionInQuestions(questions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvQuestionText.setText(error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private void addQuestionInQuestions(Questions questions) {
        questionList.add(questions);
        if (questions.getId() == 31) {
            setQuestionText(questionList, 0);
            tvQuestionTitle.setText("1/32");
        }
    }

    private void setQuestionText(List<Questions> questionList, int currentQuestionNumber) {
        if (currentQuestionNumber == 32) {
            finishAttempt();
        } else {
            Questions jsonQuestionInThisMethod = questionList.get(currentQuestionNumber);
            currentQuestion.setId(jsonQuestionInThisMethod.getId());
            currentQuestion.setType(jsonQuestionInThisMethod.getType());
            currentQuestion.setQuestionText(jsonQuestionInThisMethod.getQuestionText());
            tvQuestionText.setText(jsonQuestionInThisMethod.getQuestionText());
            tvQuestionTitle.setText((jsonQuestionInThisMethod.getId()+1) + "/32");
        }
    }

    //СЧИТАЕМ
    public void answer(int numberButton) {

        Integer idQ =  currentQuestion.getId();

        if ((currentQuestion.getType()).equals("JP")) {
            JP += numberButton;
            Log.e("жепе", "" + JP + "");
        } else {
            if ((currentQuestion.getType()).equals("SN")) {
                SN += numberButton;
            } else {
                if ((currentQuestion.getType()).equals("EI")) {
                    EI += numberButton;
                } else TF += numberButton;
            }
        }

        Log.e("непажилой параметр", currentQuestion.getType());

        Log.e("пажилой параметр", JP + " " + SN + " " + EI + " " + TF);

        setQuestionText(questionList, idQ+1);
    }


    public void finishAttempt () {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map params = new HashMap();
        String URL = getResources().getString(R.string.URL) + "/api/testing/finishAttempt";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
        new JSONObject(params),
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getResults(response.getLong("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void getResults(long id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String params = "IE=" + EI + "&SN=" + SN + "&TF=" + TF + "&JP=" + JP + "&id=" + id;
        String url = getResources().getString(R.string.URL) + "/api/testing/results?" + params;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Here Log d", "результатики подсоситесь");
                    for (int i = 0; i < response.length(); i++) {

                        Results results = new Results();
                        JSONObject jsonObject = response.getJSONObject("results");

                        results.setDescription(jsonObject.getString(
                                "description"
                        ));
                        results.setType(jsonObject.getString("type"));
                        results.setId(jsonObject.getInt("id"));
                        goToResultActivity(results);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvQuestionText.setText(error.getMessage());
            }

        }){
        @Override
        public Map getHeaders () throws AuthFailureError {
            Map params = new HashMap();
            params.put("Authorization", "Bearer " + token);
            return params;
        }

        @Override
        public String getBodyContentType () {
            return "application/json";
        }
    };
        requestQueue.add(stringRequest);
    }

    private void goToResultActivity(Results jsonResult) {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("token_key", token);
        i.putExtra("Results", jsonResult);
        i.putExtra("isAuth", isAuth);
        startActivity(i);
    }

    private void runTimer () {
        final TextView textViewTimer = (TextView) findViewById(R.id.tvTimer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secon = seconds % 60;
                String time = String.format("%02d:%02d", minutes, secon);
                textViewTimer.setText(time);
                if (running) {
                    seconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }
}
