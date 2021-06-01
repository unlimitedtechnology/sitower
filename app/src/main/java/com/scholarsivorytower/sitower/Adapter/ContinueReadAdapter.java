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

public class ContinueReadAdapter extends RecyclerView.Adapter<ContinueReadAdapter.MyViewHolder> {

    private List<Result> ContinueReadList;
    Context mcontext;
    PrefManager prefManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_bookname;
        ImageView iv_thumb;

        public MyViewHolder(View view) {
            super(view);
            txt_bookname = (TextView) view.findViewById(R.id.txt_bookname);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        }
    }


    public ContinueReadAdapter(Context context, List<Result> ContinueReadList) {
        this.ContinueReadList = ContinueReadList;
        this.mcontext = context;
        prefManager = new PrefManager(mcontext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.continue_read_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txt_bookname.setText("" + ContinueReadList.get(position).getTitle());

        Picasso.get().load(ContinueReadList.get(position).getImage()).into(holder.iv_thumb);

        holder.iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "call");
                Intent intent = new Intent(mcontext, BookDetails.class);
                intent.putExtra("ID", ContinueReadList.get(position).getId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ContinueReadList.size();
    }

}
