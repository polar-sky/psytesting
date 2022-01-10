package info.fandroid.quizapp.quizapplication.activity;

import android.app.Activity;
import android.content.Context;
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
import info.fandroid.quizapp.quizapplication.json.Answers;
import info.fandroid.quizapp.quizapplication.json.Questions;


public class QuizActivity extends BaseActivity {

    private int JP;

    private Activity mActivity;
    private Context mContext;
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
        Intent intent = getIntent();
        //token = intent.getStringExtra("token_key");
        //isAuth = intent.getStringExtra("isAuth");\

        //TOKEN BLYAT
        Bundle arguments = getIntent().getExtras();
        token = arguments.get("token_key").toString();
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

  public void getAnswers () {
/*        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/answers";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("answers");
                            for (int i = 0; i<jsonArray.length();i++) {
                                Answers answers = new Answers();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                answers.setAnswerText(jsonObject.getString("answerText"));
                                answers.setNumber(jsonObject.getInt("number"));
                                switch (answers.getNumber()) {
                                    case 1:
                                        answerBtn1.setText(answers.getAnswerText());
                                        break;
                                    case 2:
                                        answerBtn2.setText(answers.getAnswerText());
                                        break;
                                    case 3:
                                        answerBtn3.setText(answers.getAnswerText());
                                        break;
                                    case 4:
                                        answerBtn4.setText(answers.getAnswerText());
                                        break;
                                    case 5:
                                        answerBtn5.setText(answers.getAnswerText());
                                        break;
                                }*/
                                //getQuestion();
   /*                         }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvQuestionText.setText("That didn't work!");
            }
        });*/
        //queue.add(stringRequest);
    }

    public void getQuestion() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.URL) + "/api/testing/questions";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("questions");
                    for (int i = 0; i<jsonArray.length();i++) {
                        Questions questions = new Questions();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                tvQuestionText.setText(error.getMessage() + "пашол нахуй андроид");
            }
        });
        queue.add(stringRequest);
    }
    private void addQuestionInQuestions(Questions questions) {
        questionList.add(questions);
        if (questions.getId() == 32) {
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
            tvQuestionTitle.setText(jsonQuestionInThisMethod.getId() + "/32");
        }
    }
    public void answer(int numberButton) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        /*params.put("idA", numberButton);
        params.put("idQ", Long.toString(currentQuestion.getId()));
        String URL =  getResources().getString(R.string.URL) + "/api/test/giveAnswer";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!response.equals(null)) {
                            setQuestionText(questionList,currentQuestion.getId());
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
        requestQueue.add(request);*/
    }
    public void finishAttempt() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map params = new HashMap();
        String URL =  getResources().getString(R.string.URL) + "/api/test/finishAttempt";
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getResultRequest(response.getLong("id"));
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
        requestQueue.add(request);*/
    }

    private void runTimer() {
        final TextView textViewTimer = (TextView) findViewById(R.id.tvTimer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds%3600)/60;
                int secon = seconds%60;
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
