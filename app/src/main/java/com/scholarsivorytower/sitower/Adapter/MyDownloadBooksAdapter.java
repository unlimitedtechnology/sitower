package com.scholarsivorytower.sitower.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scholarsivorytower.sitower.Model.BookModel.Result;
import com.scholarsivorytower.sitower.R;
import com.scholarsivorytower.sitower.Utility.OnClick;
import com.scholarsivorytower.sitower.Utility.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyDownloadBooksAdapter extends RecyclerView.Adapter<MyDownloadBooksAdapter.MyViewHolder> {

    private List<Result> NewArrivalList;
    Context mcontext;
    PrefManager prefManager;
    String from;
    OnClick onClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_bookname;
        ImageView iv_thumb;

        public MyViewHolder(View view) {
            super(view);
            txt_bookname = (TextView) view.findViewById(R.id.txt_bookname);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        }
    }


    public MyDownloadBooksAdapter(Context context, List<Result> NewArrivalList, String from,
                                  OnClick onClick) {
        this.NewArrivalList = NewArrivalList;
        this.mcontext = context;
        this.from = from;
        this.onClick=onClick;
        prefManager = new PrefManager(mcontext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (from.equalsIgnoreCase("Home")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mydownloadbook_item2, parent, false);
            return new MyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mydownloadbook_item2, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_bookname.setText("" + NewArrivalList.get(position).getTitle());

        Picasso.get().load(NewArrivalList.get(position).getImage()).into(holder.iv_thumb);

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call");
                onClick.OnClick("read",position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return NewArrivalList.size();
    }

}
