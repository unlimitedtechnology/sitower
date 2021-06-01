package com.scholarsivorytower.sitower.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.scholarsivorytower.sitower.Model.BannerModel.Result;
import com.scholarsivorytower.sitower.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Activity context;
    private List<Result> mBennerList;

    public BannerAdapter(Activity context, List<Result> itemChannels) {
        this.context = context;
        this.mBennerList = itemChannels;
        inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mBennerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.banner_item_row, container, false);
        assert imageLayout != null;

        ImageView imageView = imageLayout.findViewById(R.id.image);

        Picasso.get().load(mBennerList.get(position).getImage())
                .into(imageView);

        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }

}