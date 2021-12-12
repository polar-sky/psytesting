package info.fandroid.quizapp.quizapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.constants.AppConstants;
import info.fandroid.quizapp.quizapplication.listeners.WebListener;
import info.fandroid.quizapp.quizapplication.web.WebEngine;

public class CustomUrlActivity extends BaseActivity  {
    private Activity activity;
    private Context context;
    private String pageTitle, pageUrl;

    private WebView webView;
    private WebEngine webEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();

    }

    private void initVar() {
        activity = CustomUrlActivity.this;
        context = activity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            pageTitle = intent.getStringExtra(AppConstants.BUNDLE_KEY_TITLE);
            pageUrl = intent.getStringExtra(AppConstants.BUNDLE_KEY_URL);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_custom_url);
        initWebEngine();
        initLoader();
        initToolbar(true);
        setToolbarTitle(pageTitle);
        enableUpButton();
    }

    public void initWebEngine() {
        webView = (WebView) findViewById(R.id.webView);

        webEngine = new WebEngine(webView, activity);
        webEngine.initWebView();

        webEngine.initListeners(new WebListener() {
            @Override
            public void onStart() {
                showLoader();
            }

            @Override
            public void onLoaded() {
                hideLoader();
            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onNetworkError() {
                showEmptyView();
            }

            @Override
            public void onPageTitle(String title) {

            }
        });
    }

    private void initFunctionality() {
        webEngine.loadPage(pageUrl);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}