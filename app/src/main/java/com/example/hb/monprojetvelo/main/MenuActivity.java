package com.example.hb.monprojetvelo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.example.hb.monprojetvelo.R;

public class MenuActivity extends AppCompatActivity {

    private Spinner menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       // menu =(Spinner) findViewById(R.id.spinner);
    }
}
