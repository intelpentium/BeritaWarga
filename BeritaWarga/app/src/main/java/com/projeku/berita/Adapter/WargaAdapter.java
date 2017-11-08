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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fathurrahman on 10/08/2017.
 */

public class WargaAdapter extends RecyclerView.Adapter<WargaAdapter.MyViewHolder>{

    private Context mContext;
    private List<WargaModel> wargaModelList;

    String getid_user;
    int getID;

    String url_del = "http://sdbundamulia.com/ws_berita/DeleteWarga.php";

    public WargaAdapter(Context mContext, List<WargaModel> wargaModelList) {
        this.mContext = mContext;
        this.wargaModelList = wargaModelList;
    }

    @Override
    public WargaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daftar_warga_item, parent, false);

        return new WargaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WargaAdapter.MyViewHolder holder, final int position) {
        final WargaModel all = wargaModelList.get(position);

        holder.nama.setText(all.nama);
        holder.email.setText(all.email);

        Glide.with(mContext).load(all.photo)
                .error(R.drawable.error_image)
                .into(holder.images);

        holder.cardView.setOnClickListener(onClickListener(all.ID));

        SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        String id_level = preferences.getString("id_level","Not Available");

        if(id_level.equals("5")){

            holder.overflow.setVisibility(View.VISIBLE);
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getid_user  = String.valueOf(all.ID);
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
        ImageView overflow;
        TextView nama, email;
        CardView cardView;
        CircleImageView images;

        public MyViewHolder(View view) {
            super(view);

            overflow   = (ImageView) view.findViewById(R.id.overflow);
            nama        = (TextView) view.findViewById(R.id.nama);
            email      = (TextView) view.findViewById(R.id.email);
            cardView       = (CardView) view.findViewById(R.id.cardView);
            images       = (CircleImageView) view.findViewById(R.id.images);
        }
    }

    @Override
    public int getItemCount() {
        return wargaModelList.size();
    }

    public void delete(int position) { //removes the row
        wargaModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wargaModelList.size());
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

//                Toast.makeText(mContext, ""+s2, Toast.LENGTH_LONG).show();

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
                par2.put("id_user", getid_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_del, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
