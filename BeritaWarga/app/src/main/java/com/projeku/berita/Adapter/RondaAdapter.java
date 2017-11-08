package com.projeku.berita.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projeku.berita.HomeSatpam;
import com.projeku.berita.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fathurrahman on 29/04/2017.
 */

public class RondaAdapter extends RecyclerView.Adapter<RondaAdapter.MyViewHolder>{

    String url_delete = "http://sdbundamulia.com/ws_berita/DeleteRonda.php";
    String getid_ronda;

    private Context mContext;
    private List<RondaModel> rondaModelList;

    public RondaAdapter(Context mContext, List<RondaModel> rondaModelList) {
        this.mContext = mContext;
        this.rondaModelList = rondaModelList;
    }

    @Override
    public RondaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ronda_sen_item, parent, false);

        return new RondaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RondaAdapter.MyViewHolder holder, int position) {
        final RondaModel all = rondaModelList.get(position);

        holder.nama.setText(all.nama);

        SharedPreferences preferences = mContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        String id_level = preferences.getString("id_level","Not Available");

        if(id_level.equals("2") || id_level.equals("5")){
            holder.hapusBtn.setVisibility(View.VISIBLE);
        }

        holder.hapusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Confirm Hapus");
                alertDialog.setMessage("Apakah anda yakin ?");
//                    alertDialog.setIcon(R.drawable.delete);

                alertDialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        getid_ronda = String.valueOf(all.ID);
                        Delete();
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

//        holder.cardView.setOnClickListener(onClickListener(all.ID));

//        holder.mSlideView.setOnFinishListener(new SlideView.OnFinishListener() {
//            @Override
//            public void onFinish() {
//                //someting to do
//
//                Toast.makeText(mContext, ""+all.nama, Toast.LENGTH_LONG).show();
//            }
//        });
    }


    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "Position " + position, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(mContext, RondaSenDetail.class);
//                mContext.startActivity(i);
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        CardView cardView;
        Button hapusBtn;

//        SlideView mSlideView;

        public MyViewHolder(View view) {
            super(view);

            nama        = (TextView) view.findViewById(R.id.nama);
            cardView       = (CardView) view.findViewById(R.id.cardView);
            hapusBtn       = (Button) view.findViewById(R.id.hapusBtn);

//            mSlideView = (SlideView) view.findViewById(R.id.slide_view);

        }
    }

    @Override
    public int getItemCount() {
        return rondaModelList.size();
    }

    public void Delete() {

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

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Toast.makeText(mContext, "Data berhasil dihapus", Toast.LENGTH_LONG ).show();
                        Intent i = new Intent(mContext, HomeSatpam.class);
                        mContext.startActivity(i);
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_ronda", getid_ronda);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url_delete, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }
}
