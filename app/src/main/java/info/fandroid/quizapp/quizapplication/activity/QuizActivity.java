package info.fandroid.quizapp.quizapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.adapters.QuizAdapter;
import info.fandroid.quizapp.quizapplication.constants.AppConstants;
import info.fandroid.quizapp.quizapplication.data.preference.AppPreference;
import info.fandroid.quizapp.quizapplication.listeners.ListItemClickListener;
import info.fandroid.quizapp.quizapplication.models.quiz.QuizModel;
import info.fandroid.quizapp.quizapplication.utilities.ActivityUtilities;
import info.fandroid.quizapp.quizapplication.utilities.BeatBox;
import info.fandroid.quizapp.quizapplication.utilities.DialogUtilities;
import info.fandroid.quizapp.quizapplication.utilities.SoundUtilities;


public class QuizActivity extends BaseActivity implements  DialogUtilities.OnCompleteListener {

    private Activity mActivity;
    private Context mContext;
    private ImageButton btnSpeaker;
    private Button btnNext;
    private RecyclerView mRecyclerQuiz;
    private TextView tvQuestionText;
    private TextView tvQuestionTitle;
    private ImageView imgFirstLife, imgSecondLife, imgThirdLife, imgFourthLife, imgFifthLife;

    private QuizAdapter mAdapter = null;
    private List<QuizModel> mItemList;
    ArrayList<String> mOptionList;
    ArrayList<String> mBackgroundColorList;

    private int mQuestionPosition = 0;
    private int mQuestionsCount = 0;
    private int mScore = 0, mWrongAns = 0, mSkip = 0;
    private boolean mUserHasPressed = false;
    private boolean mIsSkipped = false;
    private String mQuestionText, mGivenAnsText,  mCategoryId;

    private BeatBox mBeatBox;
    private List<SoundUtilities> mSounds;
    private boolean isSoundOn;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: initializeRewardedAds();
        //TODO: loadRewardedVideoAds();

        initVar();
        initView();
        loadData();
        initListener();


    }

    private void initVar() {
        mActivity = QuizActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            mCategoryId = intent.getStringExtra(AppConstants.BUNDLE_KEY_INDEX);
        }

        mItemList = new ArrayList<>();
        mOptionList = new ArrayList<>();
        mBackgroundColorList = new ArrayList<>();
        //TODO: mResultList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_quiz);

        btnNext = (Button) findViewById(R.id.btnNext);

        tvQuestionText = (TextView) findViewById(R.id.tvQuestionText);
        tvQuestionTitle = (TextView) findViewById(R.id.tvQuestionTitle);

        mRecyclerQuiz = (RecyclerView) findViewById(R.id.rvQuiz);
        mRecyclerQuiz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new QuizAdapter(mContext, mActivity, mOptionList, mBackgroundColorList);
        mRecyclerQuiz.setAdapter(mAdapter);

        initToolbar(true);
        setToolbarTitle(getString(R.string.quiz));
        enableUpButton();
        initLoader();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadData() {
        showLoader();

        loadJson();
    }

    public void initListener() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mUserHasPressed) {
                    FragmentManager manager = getSupportFragmentManager();
                    DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.skip_text), getString(R.string.skip_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_SKIP_OPTION);
                    dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
                } else {
                    //TODO: updateResultSet();
                    setNextQuestion();
                }
            }
        });

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (!mUserHasPressed) {
                    int clickedAnswerIndex = position;

                        for (int currentItemIndex = 0; currentItemIndex < mOptionList.size(); currentItemIndex++) {

                                mScore++;
                                ((LinearLayoutManager) mRecyclerQuiz.getLayoutManager()).scrollToPosition(currentItemIndex);
                        }

                    mGivenAnsText = mItemList.get(mQuestionPosition).getAnswers().get(clickedAnswerIndex);

                    mUserHasPressed = true;
                    mAdapter.notifyDataSetChanged();
                }
            }

        });

    }



    public void setNextQuestion() {
        if (isSoundOn) {
            mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_FIRST_INDEX));
        }
        mUserHasPressed = false;
        if (mQuestionPosition < mItemList.size() - 1 ) {
            mQuestionPosition++;
            updateQuestionsAndAnswers();
        } else if (mQuestionPosition < mItemList.size() - 1 ) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.reward_dialog_title), getString(R.string.reward_dialog_message), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_REWARD_OPTION);
            dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
        } else {
            //TODO: invoke ScoreCardActivity
        }
    }

    public void updateQuestionsAndAnswers() {
        mOptionList.clear();
        ((LinearLayoutManager) mRecyclerQuiz.getLayoutManager()).scrollToPosition(AppConstants.BUNDLE_KEY_ZERO_INDEX);

        mOptionList.addAll(mItemList.get(mQuestionPosition).getAnswers());
        mAdapter.notifyDataSetChanged();

        mQuestionText = mItemList.get(mQuestionPosition).getQuestion();

        tvQuestionText.setText(Html.fromHtml(mQuestionText));
        tvQuestionTitle.setText(getString(R.string.quiz_question_title, mQuestionPosition + 1, mQuestionsCount));
    }

    public void quizActivityClosePrompt() {
        FragmentManager manager = getSupportFragmentManager();
        DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.exit), getString(R.string.cancel_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_CLOSE_OPTION);
        dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
    }

    private void loadJson() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.QUESTION_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseJson(sb.toString());
    }

    public void parseJson(String jsonData) {
        try {

            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_QUESTIONNAIRY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String question = jsonObj.getString(AppConstants.JSON_KEY_QUESTION);
                String categoryId = jsonObj.getString(AppConstants.JSON_KEY_CATEGORY_ID);

                Log.d("TAG", categoryId.toString());

                JSONArray jsonArray2 = jsonObj.getJSONArray(AppConstants.JSON_KEY_ANSWERS);
                ArrayList<String> contents = new ArrayList<>();
                for (int j = 0; j < jsonArray2.length(); j++) {
                    String item_title = jsonArray2.get(j).toString();
                    contents.add(item_title);
                }
                if (mCategoryId.equals(categoryId)) {
                    mItemList.add(new QuizModel(question, contents, categoryId));
                }
            }

            mQuestionsCount = mItemList.size();
            Collections.shuffle(mItemList);

            hideLoader();
            updateQuestionsAndAnswers();

        } catch (JSONException e) {
            e.printStackTrace();
            showEmptyView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                quizActivityClosePrompt();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        quizActivityClosePrompt();
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            if (viewIdText.equals(AppConstants.BUNDLE_KEY_CLOSE_OPTION)) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            } else if (viewIdText.equals(AppConstants.BUNDLE_KEY_SKIP_OPTION)) {
                mSkip++;
                //TODO: mIsSkipped = true;
                mGivenAnsText = getResources().getString(R.string.skipped_text);
                //TODO: updateResultSet();
                setNextQuestion();
            } else if (viewIdText.equals(AppConstants.BUNDLE_KEY_REWARD_OPTION)) {
                //TODO:  mRewardedVideoAd.show();
            }
        } else if (!isOkPressed && viewIdText.equals(AppConstants.BUNDLE_KEY_REWARD_OPTION)) {
            //TODO: invoke ScoreCardActivity
            AppPreference.getInstance(mContext).setQuizResult(mCategoryId, mScore);
            AppPreference.getInstance(mContext).setQuizQuestionsCount(mCategoryId, mQuestionsCount);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
