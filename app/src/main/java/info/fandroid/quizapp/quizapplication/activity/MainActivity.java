package info.fandroid.quizapp.quizapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import info.fandroid.quizapp.quizapplication.R;
import info.fandroid.quizapp.quizapplication.adapters.CategoryAdapter;
import info.fandroid.quizapp.quizapplication.constants.AppConstants;
import info.fandroid.quizapp.quizapplication.listeners.ListItemClickListener;
import info.fandroid.quizapp.quizapplication.models.quiz.CategoryModel;
import info.fandroid.quizapp.quizapplication.utilities.ActivityUtilities;
import info.fandroid.quizapp.quizapplication.utilities.AppUtilities;

public class MainActivity extends BaseActivity {

    private Activity activity;
    private Context context;

    private Toolbar toolbar;

    private AccountHeader header = null;
    private Drawer drawer = null;

    private ArrayList<CategoryModel> categoryList;
    private CategoryAdapter adapter = null;
    private RecyclerView recyclerView;

    private String token;
    private String isAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = MainActivity.this;
        context = getApplicationContext();



        recyclerView = (RecyclerView) findViewById(R.id.rvContent);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(context, activity, categoryList);
        recyclerView.setAdapter(adapter);

        initLoader();
        loadData();
        initListener();

        //получаем ебучий токен
        Bundle arguments = getIntent().getExtras();
        token = arguments.get("token_key").toString();

        final IProfile profile = new ProfileDrawerItem().withIcon(R.drawable.ic_dev);

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        ActivityUtilities.getInstance().invokeCustomUrlActivity(activity, CustomUrlActivity.class,
                                getResources().getString(R.string.site), getResources().getString(R.string.site_url), false);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .addProfiles(profile)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("О тесте").withIcon(R.drawable.ic_dev).withIdentifier(10).withSelectable(false),

                        new SecondaryDrawerItem().withName("Пройти тест").withIcon(R.drawable.ic_youtube).withIdentifier(20).withSelectable(false),
                        new SecondaryDrawerItem().withName("Статистика").withIcon(R.drawable.ic_facebook).withIdentifier(21).withSelectable(false),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Настройки").withIcon(R.drawable.ic_settings).withIdentifier(30).withSelectable(false),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Выход").withIcon(R.drawable.ic_exit).withIdentifier(40).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 10) {
                                ActivityUtilities.getInstance().invokeNewActivity(activity, AboutDevActivity.class, false);

                            } else if (drawerItem.getIdentifier() == 20) {

                            } else if (drawerItem.getIdentifier() == 21) {
                                Intent i = new Intent(activity, StatisticActivity.class);
                                i.putExtra("token_key", token);
                                i.putExtra("isAuth", isAuth);
                                startActivity(i);
                                finish();
                            } else if (drawerItem.getIdentifier() == 30) {
                                // TODO: invoke SettingActivity
                            } else if (drawerItem.getIdentifier() == 40) {

                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withShowDrawerUntilDraggedOpened(true)
                .build();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            AppUtilities.tapPromtToExit(this);
        }
    }

    private void loadData() {
        showLoader();
        loadJson();
    }

    private void loadJson() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.CONTENT_FILE)));
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

    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_KEY_ITEMS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String categoryId = object.getString(AppConstants.JSON_KEY_CATEGORY_ID);
                String categoryName = object.getString(AppConstants.JSON_KEY_CATEGORY_NAME);

                categoryList.add(new CategoryModel(categoryId, categoryName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideLoader();
        adapter.notifyDataSetChanged();
    }


    private void initListener() {

        // recycler list item click listener
        adapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                CategoryModel model = categoryList.get(position);
                goToQuizPromptActivity(token);
                //ActivityUtilities.getInstance().invokeCommonQuizActivity(activity, QuizPromptActivity.class, model.getCategoryId(), true);

            }
        });
    }

    public void goToQuizPromptActivity(String string) {
        Intent main = new Intent(this, QuizPromptActivity.class);
        main.putExtra("token_key", string);
        startActivity(main);
        finish();
    }
}
