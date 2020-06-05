package com.example.jj.oscar_reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Review> reviewList;
    private LayoutInflater layoutInflater;

    public ReviewListViewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = layoutInflater.inflate(R.layout.reviewer_listview_item,null);

        TextView dateTextView = rowView.findViewById(R.id.reviewer_listview_date);
        TextView reviewerTextView = rowView.findViewById(R.id.reviewer_listview_reviewer);
        TextView categoryTextView = rowView.findViewById(R.id.reviewer_listview_category);
        TextView nomineeTextView = rowView.findViewById(R.id.reviewer_listview_nominee);
        TextView reviewTextView = rowView.findViewById(R.id.reviewer_listview_review);

        Review currentReview = reviewList.get(position);
        dateTextView.setText(currentReview.getDate());
        reviewerTextView.setText(currentReview.getReviewer());
        categoryTextView.setText(currentReview.getCategory());
        nomineeTextView.setText(currentReview.getNominee());
        reviewTextView.setText(currentReview.getReview());

        return rowView;

    }
}
