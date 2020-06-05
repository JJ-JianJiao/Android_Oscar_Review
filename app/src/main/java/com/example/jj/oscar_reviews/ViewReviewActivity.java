package com.example.jj.oscar_reviews;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewReviewActivity extends AppCompatActivity{

    private static final String TAG = ViewReviewActivity.class.getSimpleName();
//    private static final String BASE_URL = "http://www.youcode.ca/";
    private String category;
    private String hostUrl;
    private String pathUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        if(getIntent()!=null && getIntent().hasExtra(getString(R.string.transfer_category))){
            category = getIntent().getStringExtra(getString(R.string.transfer_category));
        }
        getPreferencesAndSetData();
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(hostUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            YoucodeOScarService service = retrofit.create(YoucodeOScarService.class);
            Call<String> getCall = service.listJSONServlet(pathUrl, category);
            getCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body();
                        List<Review> reviewList = new ArrayList<>();
                        String[] reviewArray = responseBody.split("\r\n");
                        int countItem = reviewArray.length / 5;
                        if (countItem != 0 && reviewArray.length % 5 == 0) {
                            for (int i = 1; i <= countItem; i++) {
                                Review currentReview = new Review();
                                currentReview.setDate(reviewArray[(i - 1) * 5]);
                                currentReview.setReviewer(reviewArray[(i - 1) * 5 + 1]);

                                String categoryDisplay = getCategoryConvert(reviewArray[(i - 1) * 5 + 2]);
                                currentReview.setCategory(categoryDisplay);
                                currentReview.setNominee(reviewArray[(i - 1) * 5 + 3]);
                                currentReview.setReview(reviewArray[(i - 1) * 5 + 4]);
                                reviewList.add(currentReview);
                            }
                            ListView reviewListView = findViewById(R.id.activity_view_listview);
                            ReviewListViewAdapter reviewListViewAdapter = new ReviewListViewAdapter(getApplicationContext(), reviewList);
                            reviewListView.setAdapter(reviewListViewAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.wrong_data_content, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_no_data, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.error_call_service_fail, Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.error_illeagle_url_review, Toast.LENGTH_SHORT).show();
        }
    }

    private String getCategoryConvert(String value){
        String[] categoryValues =getResources().getStringArray(R.array.radio_button_values);
        String[] categoryDisplay = getResources().getStringArray(R.array.radio_button_display);

        for(int i = 0; i<categoryValues.length;i++){
            if(value.equalsIgnoreCase(categoryValues[i])){
                return categoryDisplay[i];
            }
        }
        return value;
    }

    private void getPreferencesAndSetData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String backgroundColorPref = prefs.getString(getString(R.string.background_color_pref),"White");
        String backgroundColorPref = prefs.getString(getString(R.string.background_color_pref),getString(R.string.backgourd_color_default));
//        String urlPref = prefs.getString(getString(R.string.url_pref),"http://www.youcode.ca/Lab01Servlet");
        String urlPref = prefs.getString(getString(R.string.url_pref),getString(R.string.ULR_default)).trim();
        if(backgroundColorPref!=null&&!backgroundColorPref.isEmpty()) {
            ConstraintLayout layout = findViewById(R.id.activity_view_review_layout);
            layout.setBackgroundColor(Color.parseColor(backgroundColorPref));
        }
        try {
            URL url = new URL(urlPref);
            pathUrl = url.getPath().substring(1,url.getPath().length());
            hostUrl = urlPref.substring(0,urlPref.indexOf(pathUrl));
        } catch (MalformedURLException e) {
//            Toast.makeText(getApplicationContext(),"Error calling web service, please try again",Toast.LENGTH_SHORT).show();
            pathUrl = "";
            hostUrl = "";
        }
    }
}
