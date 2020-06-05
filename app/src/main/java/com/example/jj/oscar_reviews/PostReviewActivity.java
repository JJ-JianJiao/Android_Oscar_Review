package com.example.jj.oscar_reviews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostReviewActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = PostReviewActivity.class.getSimpleName();
    private static final String PASSWORD = "oscar275";

    private EditText nomineeEditText;
    private EditText reviewEditText;
    private RadioGroup categoryRadioGroup;
    private Button submitButton;

    private String reviewerStr;
    private String nomineeStr;
    private String reviewStr;
    private String passwordStr;
    private String categoryStr;


    private String hostUrl;
    private String pathUrl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_view_review:
                if(categoryStr!=null&&!categoryStr.isEmpty()) {
                    Intent listJittersIntent = new Intent(this, ViewReviewActivity.class);
                    listJittersIntent.putExtra(getString(R.string.transfer_category), categoryStr);
                    startActivity(listJittersIntent);
                    return true;
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.need_choose_category,Toast.LENGTH_SHORT).show();
                    return false;
                }
            case R.id.menu_item_preference:
                Intent postJittersIntent = new Intent(this,PreferencesActivity.class);
                startActivity(postJittersIntent);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomineeEditText = findViewById(R.id.nominee_edit);
        reviewEditText = findViewById(R.id.review_edit);
        categoryRadioGroup = findViewById(R.id.category_radiogroup);
        submitButton = findViewById(R.id.submit_button);
        getPreferencesAndSetData();
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                String tempStr = rb.getText().toString();
                String[] categoryValues =getResources().getStringArray(R.array.radio_button_values);
                if(tempStr.equals(getString(R.string.best_picture))){
                    categoryStr = categoryValues[0];
                }
                else if(tempStr.equals(getString(R.string.best_actor))){
                    categoryStr = categoryValues[1];
                }
                else if(tempStr.equals(getString(R.string.best_actress))){
                    categoryStr = categoryValues[2];
                }
                else if(tempStr.equals(getString(R.string.film_editing))){
                    categoryStr = categoryValues[3];
                }
                else if(tempStr.equals(getString(R.string.visual_effects))){
                    categoryStr = categoryValues[4];
                }
                //Toast.makeText(getApplicationContext(),categoryStr,Toast.LENGTH_SHORT).show();
            }
        });

        if(submitButton!=null)
            submitButton.setOnClickListener( (View view) -> {
                reviewStr = reviewEditText.getText().toString().trim();
                nomineeStr = nomineeEditText.getText().toString().trim();
                if(reviewerStr == null ||reviewerStr.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.warning_need_reviewer,Toast.LENGTH_SHORT).show();
                }
                else if(categoryStr == null || categoryStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.warning_need_category,Toast.LENGTH_SHORT).show();
                }
                else if(nomineeStr == null || nomineeStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.warning_need_nominee,Toast.LENGTH_SHORT).show();
                }
                else if(reviewStr == null ||reviewStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.warning_need_review,Toast.LENGTH_SHORT).show();
                }
                else if(passwordStr == null ||passwordStr.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.warning_need_password,Toast.LENGTH_SHORT).show();
                }else{
                    if(passwordStr.equals(PASSWORD)){
                        try {
                            //Toast.makeText(getApplicationContext(),"Do post action",Toast.LENGTH_SHORT).show();
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(hostUrl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .build();
                            YoucodeOScarService service = retrofit.create(YoucodeOScarService.class);
                            Call<String> postCall = service.postReview(pathUrl, reviewStr, reviewerStr, nomineeStr, categoryStr, passwordStr);
                            postCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), R.string.success_fetch_data, Toast.LENGTH_SHORT).show();
                                        nomineeEditText.setText("");
                                        reviewEditText.setText("");
                                        getPreferencesAndSetData();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), R.string.warning_failure_post_data, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), R.string.warning_post_failure, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), R.string.error_url_illeagal,Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.warning_password_wrong,Toast.LENGTH_SHORT).show();
                    }
                }

            });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getPreferencesAndSetData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }


    private void getPreferencesAndSetData(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String backgroundColorPref = sharedPreferences.getString(getString(R.string.background_color_pref),getString(R.string.backgourd_color_default));
        String reviewerPref = sharedPreferences.getString(getString(R.string.login_name_pref),null);
        String urlPref = sharedPreferences.getString(getString(R.string.    url_pref),getString(R.string.ULR_default)).trim();
        String passwordPref = sharedPreferences.getString(getString(R.string.password_pref),null);

        if(backgroundColorPref!=null&&!backgroundColorPref.isEmpty()) {
            LinearLayout layout = findViewById(R.id.activity_main_layout);
            layout.setBackgroundColor(Color.parseColor(backgroundColorPref));
        }
        if(reviewerPref!=null&&!reviewerPref.isEmpty())
            reviewerStr = reviewerPref;
        if(passwordPref!=null&&!passwordPref.isEmpty())
            passwordStr = passwordPref;
        try {
            URL url = new URL(urlPref);
            pathUrl = url.getPath().substring(1,url.getPath().length());
            hostUrl = urlPref.substring(0,urlPref.indexOf(pathUrl));
        } catch (MalformedURLException e) {
            pathUrl = "";
            hostUrl = "";
        }
    }
}


