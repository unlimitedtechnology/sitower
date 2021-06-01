package com.scholarsivorytower.sitower.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scholarsivorytower.sitower.Activity.BookDetails;
import com.scholarsivorytower.sitower.Model.BookModel.Result;
import com.scholarsivorytower.sitower.R;
import com.scholarsivorytower.sitower.Utility.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthorBookAdapter extends RecyclerView.Adapter<AuthorBookAdapter.MyViewHolder> {

    private List<Result> NewArrivalList;
    Context mcontext;
    PrefManager prefManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_bookname, txt_view;
        ImageView iv_thumb;

        public MyViewHolder(View view) {
            super(view);
            txt_bookname = (TextView) view.findViewById(R.id.txt_bookname);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
//            txt_view = (TextView) view.findViewById(R.id.txt_view);
        }
    }


    public AuthorBookAdapter(Context context, List<Result> NewArrivalList) {
        this.NewArrivalList = NewArrivalList;
        this.mcontext = context;
        prefManager = new PrefManager(mcontext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.authorbook_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.txt_view.setText("" + NewArrivalList.get(position).getReadcnt());
        holder.txt_bookname.setText("" + NewArrivalList.get(position).getTitle());
        Picasso.get().load(NewArrivalList.get(position).getImage()).into(holder.iv_thumb);

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call");
                Intent intent = new Intent(mcontext, BookDetails.class);
                intent.putExtra("ID", NewArrivalList.get(position).getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return NewArrivalList.size();
    }

}
