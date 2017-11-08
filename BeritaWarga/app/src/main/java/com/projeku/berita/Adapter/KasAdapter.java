package com.projeku.berita.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projeku.berita.KasDetail;
import com.projeku.berita.R;

import java.util.List;

/**
 * Created by Fathurrahman on 28/04/2017.
 */

public class KasAdapter extends RecyclerView.Adapter<KasAdapter.MyViewHolder>{

    private Context mContext;
    private List<KasModel> KasModelList;

    public KasAdapter(Context mContext, List<KasModel> KasModelList) {
        this.mContext = mContext;
        this.KasModelList = KasModelList;
    }

    @Override
    public KasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kas_item, parent, false);

        return new KasAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final KasAdapter.MyViewHolder holder, int position) {
        final KasModel all = KasModelList.get(position);

        holder.nama.setText(all.nama);

        holder.cardView.setOnClickListener(onClickListener(all.ID, all.email));

//        holder.mSlideView.setOnFinishListener(new SlideView.OnFinishListener() {
//            @Override
//            public void onFinish() {
//                //someting to do
//
//                Toast.makeText(mContext, ""+all.nama, Toast.LENGTH_LONG).show();
//            }
//        });
    }


    private View.OnClickListener onClickListener(final int position, final String email) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("email", email);
                editor.commit();

//                Toast.makeText(mContext, "Position " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, KasDetail.class);
                mContext.startActivity(i);
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        CardView cardView;

//        SlideView mSlideView;

        public MyViewHolder(View view) {
            super(view);

            nama        = (TextView) view.findViewById(R.id.nama);
            cardView       = (CardView) view.findViewById(R.id.cardView);

//            mSlideView = (SlideView) view.findViewById(R.id.slide_view);

        }
    }

    @Override
    public int getItemCount() {
        return KasModelList.size();
    }
}
