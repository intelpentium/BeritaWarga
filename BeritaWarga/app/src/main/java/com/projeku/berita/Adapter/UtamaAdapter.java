package com.projeku.berita.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fathurrahman on 23/04/2017.
 */

public class UtamaAdapter extends RecyclerView.Adapter<UtamaAdapter.MyViewHolder>{

    private Context mContext;
    private List<UtamaModel> UtamaModelList;

    String getid_berita;
    int getID;

    String url_del = "http://sdbundamulia.com/ws_berita/DeleteBerita.php";

    public UtamaAdapter(Context mContext, List<UtamaModel> UtamaModelList) {
        this.mContext = mContext;
        this.UtamaModelList = UtamaModelList;
    }

    @Override
    public UtamaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.warga_utama_item, parent, false);

        return new UtamaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UtamaAdapter.MyViewHolder holder, final int position) {
        final UtamaModel all = UtamaModelList.get(position);

        holder.judul.setText(all.judul);
        holder.berita.setText(all.berita);
        holder.tgl.setText(all.tgl);

        Glide.with(mContext).load(all.imageView)
                .error(R.drawable.error_image)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(onClickListener(all.ID));

        SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        String id_level = preferences.getString("id_level","Not Available");

        if(id_level.equals("5")){

            holder.overflow.setVisibility(View.VISIBLE);
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getid_berita  = String.valueOf(all.ID);
                    getID = position;

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Konfirmasi Hapus");
                    alertDialog.setMessage("Apakah anda yakin ?");
//                    alertDialog.setIcon(R.drawable.delete);

                    alertDialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

//                            deleteData();
                            Delete();
                            delete(getID);
                        }
                    });
                    alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                }
            });
        }
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext, "Position " + position, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(mContext, AllDealsDetail.class);
//                mContext.startActivity(i);
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, overflow;
        TextView judul, berita, tgl;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            imageView   = (ImageView) view.findViewById(R.id.images);
            overflow   = (ImageView) view.findViewById(R.id.overflow);
            judul        = (TextView) view.findViewById(R.id.judul);
            berita      = (TextView) view.findViewById(R.id.berita);
            tgl          = (TextView) view.findViewById(R.id.tgl);
            cardView       = (CardView) view.findViewById(R.id.cardView);
        }
    }

    @Override
    public int getItemCount() {
        return UtamaModelList.size();
    }

    public void delete(int position) { //removes the row
        UtamaModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, UtamaModelList.size());
    }

    public void Delete() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);

//                Toast.makeText(RencanaKegiatanEdit.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

//                        SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        editor.putString("type", "Rencana");
//                        editor.commit();

//                        Intent i = new Intent(RencanaKegiatanEdit.this, Home.class);
//                        startActivity(i);
//                        finish();
//
//                        Toast.makeText(RencanaKegiatanEdit.this, "Selamat anda telah mengubah rencana kegiatan. Tunggu konfirmasi selanjutnya ", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_berita", getid_berita);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_del, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
