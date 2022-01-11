package info.fandroid.quizapp.quizapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.constants.AppConstants;
import info.fandroid.quizapp.quizapplication.data.preference.AppPreference;
import info.fandroid.quizapp.quizapplication.utilities.ActivityUtilities;


public class QuizPromptActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;
    private Button mBtnYes, mBtnNo;
    private TextView firstText, thirdtext;
    private String categoryId, score, questionsCount;
    private String token;
    private String isAuth;
    //private String questionsCount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initListener();

        //получаем токен
        Bundle arguments = getIntent().getExtras();
        token = arguments.get("token_key").toString();
        Log.e("ТОКЕН", token + "");
        //isAuth = arguments.get("isAuth").toString();
    }

    private void initVar() {
        mActivity = QuizPromptActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getStringExtra(AppConstants.BUNDLE_KEY_INDEX);
            score = AppPreference.getInstance(mContext).getString(categoryId);
            questionsCount = AppPreference.getInstance(mContext).getString(categoryId + AppConstants.QUESTIONS_IN_TEST);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_quiz_prompt);

        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);

        firstText = (TextView) findViewById(R.id.first_text);
        thirdtext = (TextView) findViewById(R.id.third_text);

        if (score != null && questionsCount != null) {
            firstText.setText(getString(R.string.quiz_promt_first_text, score, questionsCount));
            thirdtext.setText(R.string.quiz_promt_third_text);
        }

        initToolbar(true);
        setToolbarTitle(getString(R.string.quiz_prompt));
        enableUpButton();
    }


    private void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTestActivity(isAuth);
               //ActivityUtilities.getInstance().invokeCommonQuizActivity(mActivity, QuizActivity.class, categoryId, true);
            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
    }

    public void goToTestActivity(String isAuth) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL =  getResources().getString(R.string.URL) + "/api/testing/startAttempt";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        goQuiz();
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
        };
        requestQueue.add(request);

    }

    public void goQuiz(){
        Intent test = new Intent(this, QuizActivity.class);
        test.putExtra("token_key", token);
        //test.putExtra("isAuth", isAuth);
        startActivity(test);
    }

}
