package info.fandroid.quizapp.quizapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.json.Results;

public class ResultActivity extends AppCompatActivity {

    private String token;
    private String isAuth;
    private Results jsonResult;

    private TextView tvResult;
    private TextView tvResultText;
    private Button btnGoMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        token = intent.getStringExtra("token_key");
        isAuth = intent.getStringExtra("isAuth");
        jsonResult = (Results) intent.getSerializableExtra("JSONResult");

        tvResult = findViewById(R.id.tvResult);
        tvResultText = findViewById(R.id.tvResultText);
        btnGoMain = findViewById(R.id.btnGoMain);

        View.OnClickListener btnGoMainClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        };

        btnGoMain.setOnClickListener(btnGoMainClick);
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("token_key", token);
        startActivity(i);
        finish();
    }

}
