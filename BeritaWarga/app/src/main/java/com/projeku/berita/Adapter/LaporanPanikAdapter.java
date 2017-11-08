package com.projeku.berita.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projeku.berita.ActivityMapNotif;
import com.projeku.berita.BeritaKejahatanAdd;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fathurrahman on 10/08/2017.
 */

public class LaporanPanikAdapter extends RecyclerView.Adapter<LaporanPanikAdapter.MyViewHolder>{

    private Context mContext;
    private List<LaporanPanikModel> LaporanPanikModelList;

    String getid_notif, getemail_user, getnama;
    int getID;

    String url_kirim = "http://sdbundamulia.com/ws_berita/InputDaftarNotif.php";
    String url_del = "http://sdbundamulia.com/ws_berita/DeleteDaftarNotif.php";

    public LaporanPanikAdapter(Context mContext, List<LaporanPanikModel> LaporanPanikModelList) {
        this.mContext = mContext;
        this.LaporanPanikModelList = LaporanPanikModelList;
    }

    @Override
    public LaporanPanikAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_item, parent, false);

        return new LaporanPanikAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LaporanPanikAdapter.MyViewHolder holder, final int position) {
        final LaporanPanikModel all = LaporanPanikModelList.get(position);

        holder.nama.setText(all.nama);
        holder.tgl.setText(all.tgl);

        Glide.with(mContext).load(all.photo)
                .error(R.drawable.ic_profile)
                .into(holder.images);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("email", all.email);
                editor.commit();

//                Toast.makeText(mContext, "Position " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, ActivityMapNotif.class);
                mContext.startActivity(i);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getID  = position;
                getid_notif  = String.valueOf(all.ID);
                getemail_user = all.email;
                getnama = all.nama;
                showPopupMenu(holder.overflow);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, tgl;
        CardView cardView;
        CircleImageView images;
        ImageView overflow;

        public MyViewHolder(View view) {
            super(view);

            nama        = (TextView) view.findViewById(R.id.nama);
            tgl        = (TextView) view.findViewById(R.id.tgl);
            cardView       = (CardView) view.findViewById(R.id.cardView);
            images       = (CircleImageView) view.findViewById(R.id.images);
            overflow       = (ImageView) view.findViewById(R.id.overflow);

        }
    }

    @Override
    public int getItemCount() {
        return LaporanPanikModelList.size();
    }

    public void delete(int position) { //removes the row
        LaporanPanikModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, LaporanPanikModelList.size());
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_laporan, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.pilih:

                    SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("id_laporan_notif", getid_notif);
                    editor.putString("email_pelapor", getemail_user);
                    editor.putString("nama_pelapor", getnama);
                    editor.commit();

                    Intent a = new Intent(mContext, BeritaKejahatanAdd.class);
                    mContext.startActivity(a);

//                    delete(getID);
//                    Kirim();

                    return true;
                case R.id.hapus:

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

                    return true;
                default:
            }
            return false;
        }
    }

    public void Kirim() {

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
                        Toast.makeText(mContext, "Notifikasi telah berhasil dikirim", Toast.LENGTH_LONG).show();

                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_notif", getid_notif);
                par2.put("email_user", getemail_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_kirim, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
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
                par2.put("id_notif", getid_notif);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_del, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

    public void setFilter(List<LaporanPanikModel> countryModels){
        LaporanPanikModelList = new ArrayList<>();
        LaporanPanikModelList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
