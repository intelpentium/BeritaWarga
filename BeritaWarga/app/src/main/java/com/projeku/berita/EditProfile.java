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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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
 * Created by Fathurrahman on 09/08/2017.
 */

public class EditProfile extends AppCompatActivity {

    EditText nama;

    LinearLayout LayoutLogin;
    Animation shake;

    String getnama, getimage="";

    String url = "http://sdbundamulia.com/ws_berita/EditProfile.php";
    String url_gambar = "http://sdbundamulia.com/ws_berita/UploadPhotoUser.php";

    private int PICK_IMAGE_REQUEST_1 = 1;
    private int PICK_IMAGE_REQUEST_2 = 2;
    private Bitmap bitmap = null;
    private Uri filePath=null;

    ImageView image;

    private String pictureImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutLogin = (LinearLayout) findViewById(R.id.LayoutLogin);
        shake = AnimationUtils.loadAnimation(EditProfile.this, R.anim.shake);

        nama = (EditText) findViewById(R.id.nama);
        nama.addTextChangedListener(new text(nama));

        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        getnama = preferences.getString("nama_user","Not Available");

        nama.setText(getnama);

        Button simpanBtn = (Button) findViewById(R.id.simpanBtn);
        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checknama()) {
                    LayoutLogin.startAnimation(shake);
//                    Snackbar.make(findViewById(android.R.id.content), "Nama depan tidak boleh kosong", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }
                else{

                    Edit();
                }
            }
        });

        image = (ImageView) findViewById(R.id.ImageButton);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        String photo = sharedPreferences.getString("photo","Not Available");
//        Toast.makeText(EditProfile.this, ""+photo, Toast.LENGTH_LONG).show();

        Log.e("photo",photo);
        Glide.with(EditProfile.this).load(photo)
                .error(R.drawable.camera)
                .into(image);
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
                case R.id.nama:
                    checknama();
//                    nama();
                    break;
            }
        }
    }

    private boolean checknama(){

        getnama = nama.getText().toString().trim();

        if(getnama.isEmpty()){

            nama.setError("Nama tidak boleh kosong");
            return false;
        }
        return true;
    }

    public void Edit() {

        class ParsingNegara extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfile.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s2) {
                super.onPostExecute(s2);
                loading.dismiss();

//                Toast.makeText(EditProfile.this, ""+s2, Toast.LENGTH_LONG).show();

                try {

                    String json = s2.toString(); // Respon di jadikan sebuah string
                    JSONObject jObj = new JSONObject(json); // Response di jadikan sebuah
                    String result = jObj.getString("success");

                    if(result.trim().equals("1")){

                        if(getimage.equals("")){
                            Toast.makeText(EditProfile.this, "Profile Anda berhasil diubah, Silahkan login kembali", Toast.LENGTH_LONG).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                            String type_log = sharedPreferences.getString("type_log","Not Available");

                            if(type_log.equals("Warga")){
                                Intent i = new Intent(EditProfile.this, HomeWarga.class);
                                startActivity(i);
                                finish();
                            }
                            if(type_log.equals("RT")){
                                Intent i = new Intent(EditProfile.this, HomeRt.class);
                                startActivity(i);
                                finish();
                            }
                            if(type_log.equals("Satpam")){
                                Intent i = new Intent(EditProfile.this, HomeSatpam.class);
                                startActivity(i);
                                finish();
                            }
                            if(type_log.equals("Bendahara")){
                                Intent i = new Intent(EditProfile.this, HomeBendahara.class);
                                startActivity(i);
                                finish();
                            }
                            if(type_log.equals("Admin")){
                                Intent i = new Intent(EditProfile.this, HomeAdmin.class);
                                startActivity(i);
                                finish();
                            }
                        }else {
                            uploadImage();
                        }
                    }

                }catch (Exception e) {

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String id_user = preferences.getString("id_user","Not Available");

                HashMap<String, String> par2 = new HashMap<String, String>();
                par2.put("id_user", id_user);
                par2.put("nama", getnama);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
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


//            Toast.makeText(EditProfile.this, ""+ imgFile, Toast.LENGTH_LONG).show();
//            Toast.makeText(EditProfile.this, ""+ bitmap, Toast.LENGTH_LONG).show();

//            filePath = data.getData();
//
//            if(filePath !=null){
//                try {
//
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//                    Glide.with(EditProfile.this).load(filePath)
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
                    Glide.with(EditProfile.this).load(filePath)
                            .into(image);

                    getimage="isi";

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

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(EditProfile.this, "Menambahkan Data...", "Silahkan Tunggu..." ,false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Toast.makeText(EditProfile.this, "Upload sukses", Toast.LENGTH_SHORT).show();
                Toast.makeText(EditProfile.this, "Profile Anda berhasil diubah, Silahkan login kembali", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String type_log = sharedPreferences.getString("type_log","Not Available");

                if(type_log.equals("Warga")){
                    Intent i = new Intent(EditProfile.this, HomeWarga.class);
                    startActivity(i);
                    finish();
                }
                if(type_log.equals("RT")){
                    Intent i = new Intent(EditProfile.this, HomeRt.class);
                    startActivity(i);
                    finish();
                }
                if(type_log.equals("Satpam")){
                    Intent i = new Intent(EditProfile.this, HomeSatpam.class);
                    startActivity(i);
                    finish();
                }
                if(type_log.equals("Bendahara")){
                    Intent i = new Intent(EditProfile.this, HomeBendahara.class);
                    startActivity(i);
                    finish();
                }
                if(type_log.equals("Admin")){
                    Intent i = new Intent(EditProfile.this, HomeAdmin.class);
                    startActivity(i);
                    finish();
                }

//                Intent a = new Intent(EditProfile.this, MainActivity.class);
//                startActivity(a);
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                String email_user = sharedPreferences.getString("email_user","Not Available");
                String id_user = sharedPreferences.getString("id_user","Not Available");

                HashMap<String,String> data = new HashMap<>();
                data.put("photo", uploadImage);
                data.put("email_user", email_user);
                data.put("id_user", id_user);

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
