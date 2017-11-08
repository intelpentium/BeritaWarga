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
import com.projeku.berita.HomeWarga;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fathurrahman on 25/05/2017.
 */

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder>{

    private Context mContext;
    private List<NotifModel> NotifModelList;

    String getid_notif, getemail_user;
    int getID;

    String url_kirim = "http://sdbundamulia.com/ws_berita/InputDaftarNotif.php";
    String url_del = "http://sdbundamulia.com/ws_berita/DeleteDaftarNotif.php";

    String url = "http://sdbundamulia.com/ws_berita/InputNotifStatus.php";

    public NotifAdapter(Context mContext, List<NotifModel> NotifModelList) {
        this.mContext = mContext;
        this.NotifModelList = NotifModelList;
    }

    @Override
    public NotifAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_item, parent, false);

        return new NotifAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotifAdapter.MyViewHolder holder, final int position) {
        final NotifModel all = NotifModelList.get(position);

        holder.nama.setText(all.nama);
        holder.tgl.setText(all.tgl);

        Glide.with(mContext).load(all.photo)
                .error(R.drawable.ic_profile)
                .into(holder.images);

        if(all.status.equals("Terbaca")){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.ijo_muda));
            holder.nama.setTextColor(mContext.getResources().getColor(R.color.trans));
            holder.nama.setTypeface(null, Typeface.NORMAL);
        }else{
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.nama.setTextColor(mContext.getResources().getColor(R.color.ijo));
            holder.nama.setTypeface(null, Typeface.BOLD);
        }

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

                getid_notif  = String.valueOf(all.ID);
                Input();
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getID  = position;
                getid_notif  = String.valueOf(all.ID);
                getemail_user = all.email;
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
        return NotifModelList.size();
    }

    public void delete(int position) { //removes the row
        NotifModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, NotifModelList.size());
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_pilihan, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.kirim:

//                    SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//
//                    editor.putString("id_rencana", getid_rencana);
//                    editor.commit();
//
//                    Intent a = new Intent(mContext, RencanaKegiatanEdit.class);
//                    mContext.startActivity(a);

                    delete(getID);
                    Kirim();

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

    public void setFilter(List<NotifModel> countryModels){
        NotifModelList = new ArrayList<>();
        NotifModelList.addAll(countryModels);
        notifyDataSetChanged();
    }

    public void Input() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mContext, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(mContext, ""+s2, Toast.LENGTH_LONG).show();

                try {

//                    String json = s2.toString(); // Respon di jadikan sebuah string
//                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
//                    String result = jObj.getString("success");
//
//                    if(result.trim().equals("1")){
//
//                        Toast.makeText(mContext, "Notifikasi berhasil ditambahkan", Toast.LENGTH_LONG).show();
//
////                        ActivityCompat.finishAfterTransition(mContext);
//                        SharedPreferences preferences = getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        editor.putString("type", "Notif");
//                        editor.commit();
//
//                        Intent i = new Intent(mContext, Home.class);
//                        startActivity(i);
//                        finish();
//
//                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
                String id_user = preferences.getString("id_user","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_user", id_user);
                par2.put("id_notif", getid_notif);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
