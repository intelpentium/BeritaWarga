package com.projeku.berita.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.projeku.berita.Kas;
import com.projeku.berita.R;

/**
 * Created by Fathurrahman on 10/07/2017.
 */

public class KasPeriode extends Fragment {

    String[] bahasa ={"2017 ","2018","2019","2020"};
    String getperiode;

    public static KasPeriode newInstance() {
        return new KasPeriode();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kas_periode, container, false);

        Spinner spin = (Spinner) view.findViewById(R.id.periode);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

//                Toast.makeText(getActivity(), bahasa[position], Toast.LENGTH_LONG).show();
                getperiode = bahasa[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, bahasa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("tahun", getperiode);
                editor.commit();

                Intent i = new Intent(getActivity(), Kas.class);
                startActivity(i);
            }
        });

        return view;
    }
}
