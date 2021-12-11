package info.fandroid.quizapp.quizapplication.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import info.fandroid.quizapp.quizapplication.R;

public class AboutDevActivity extends BaseActivity  {

    private TextView tvDev;
    private TextView tvDevTitle, tvDevText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);

        tvDev = (TextView) findViewById(R.id.tvDev);
        tvDevTitle = (TextView) findViewById(R.id.tvDevTitle);
        tvDevText = (TextView) findViewById(R.id.tvDevText);

        ininToolbar(true);
        setToolbarTitle(getString(R.string.about_dev));
        enableUpButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}