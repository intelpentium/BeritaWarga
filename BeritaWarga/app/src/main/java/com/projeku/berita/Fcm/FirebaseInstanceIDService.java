package com.projeku.berita.Fcm;

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;

/**
 * Created by Fathurrahman on 05/04/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        Toast.makeText(FirebaseInstanceIDService.this, token, Toast.LENGTH_LONG).show();

//        registerToken(token);
    }

//    private void registerToken(String token) {
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token",token)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://192.168.1.23/notif/register.php")
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
