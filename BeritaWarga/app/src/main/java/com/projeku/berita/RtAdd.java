package com.projeku.berita;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projeku.berita.Adapter.A_ParsingRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fathurrahman on 23/05/2017.
 */

public class RtAdd extends AppCompatActivity {

    EditText judul, berita;

    LinearLayout LayoutLogin;
    Animation shake;

    String getjudul, getberita;

    String url = "http://sdbundamulia.com/ws_berita/RtAdd.php";
    String url_gambar = "http://sdbundamulia.com/ws_berita/UploadPhotoBerita.php";

    private int PICK_IMAGE_REQUEST_1 = 1;
    private int PICK_IMAGE_REQUEST_2 = 2;
    private Bitmap bitmap = null;
    private Uri filePath=null;

    ImageButton image;

    private String pictureImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rt_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        shake = AnimationUtils.loadAnimation(RtAdd.this, R.anim.shake);

        judul = (EditText) findViewById(R.id.judul);
        judul.addTextChangedListener(new text(judul));

        berita = (EditText) findViewById(R.id.berita);
        berita.addTextChangedListener(new text(berita));

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkjudul() || !checkPass() ) {
                    LayoutLogin.startAnimation(shake);
//                    Snackbar.make(findViewById(android.R.id.con tent), "Data tidak sesuai !!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }
                else{

                    Berita();
                }
            }
        });

        image = (ImageButton) findViewById(R.id.ImageButton);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choosePhoto();
            }
        });
    }

    private class text implements TextWatcher {

        private View view;

        private text(View view){
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (view.getId()){
                case R.id.judul:
                    checkjudul();
//                    judul();
                    break;

                case R.id.berita:
                    checkPass();
//                    judul();
                    break;
            }

        }
    }

    private boolean checkjudul(){

        getjudul = judul.getText().toString().trim();

        if(getjudul.isEmpty()){

            judul.setError("Judul tidak boleh kosong");
            return false;
        }
        return true;
    }

    private boolean checkPass(){

        getberita = berita.getText().toString().trim();

        if(getberita.isEmpty()){

            berita.setError("Berita tidak boleh kosong");
            return false;
        }
        return true;
    }

    public void Berita() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RtAdd.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(RtAdd.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        Intent i = new Intent(RtAdd.this, HomeRt.class);
                        startActivity(i);
                        finish();

                        uploadImage();
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String email_user = preferences.getString("email_user","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("email_user", email_user);
                par2.put("judul", getjudul);
                par2.put("berita", getberita);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res2 = parsing.sendPostRequest(url, par2);
                return res2;
            }
        }

        ParsingNegara tambah = new ParsingNegara();
        tambah.execute();
    }

    //    ================= upload photo ==============
    private void choosePhoto() {
        final CharSequence[] options = { "Ambil Photo", "Pilih dari Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RtAdd.this);
//        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil Photo")){

                    ambilPhoto();
                }
                else if (options[item].equals("Pilih dari Gallery")){

                    gallery();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void ambilPhoto() {

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST_1);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST_1);
    }

    private void gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK){

            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setImageBitmap(bitmap);

            }


//            Toast.makeText(RtAdd.this, ""+ imgFile, Toast.LENGTH_LONG).show();
//            Toast.makeText(RtAdd.this, ""+ bitmap, Toast.LENGTH_LONG).show();

//            filePath = data.getData();
//
//            if(filePath !=null){
//                try {
//
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//                    Glide.with(RtAdd.this).load(filePath)
//                            .placeholder(R.drawable.error_image)
//                            .error(R.drawable.error_image)
//                            .into(image);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                bitmap = (Bitmap) data.getExtras().get("data");
//
//                image.setImageBitmap(bitmap);
//                image.setScaleType(ImageView.ScaleType.FIT_XY);
//            }


//            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//            bitmap = (Bitmap) data.getExtras().get("data");

//            ImageButton.setImageBitmap(bitmap);
//            ImageButton.setScaleType(ImageView.ScaleType.FIT_XY);
//            ImageButton.setAdjustViewBounds(true);
        }
        else if(requestCode == PICK_IMAGE_REQUEST_2) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    Glide.with(RtAdd.this).load(filePath)
                            .into(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

//            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                loading = ProgressDialog.show(RtAdd.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();

                Toast.makeText(RtAdd.this, "Upload sukses", Toast.LENGTH_SHORT).show();

//                Intent a = new Intent(RtAdd.this, MainActivity.class);
//                startActivity(a);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                SharedPreferences sharedPreferences = RtAdd.this.getSharedPreferences("session", Context.MODE_PRIVATE);
                String email_user = sharedPreferences.getString("email_user","Not Available");

                HashMap<String,String> data = new HashMap<>();
                data.put("photo", uploadImage);
                data.put("email_user", email_user);
//                data.put("id_user", id_user);

                A_ParsingRequest parsing = new A_ParsingRequest();
                String res = parsing.sendPostRequest(url_gambar, data);
                return res;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            // difungsikan untuk mengembalikan ke dashboard atau menu sebelumnya
            ActivityCompat.finishAfterTransition(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
