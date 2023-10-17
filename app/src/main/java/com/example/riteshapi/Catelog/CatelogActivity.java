package com.example.riteshapi.Catelog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.R;

public class CatelogActivity extends AppCompatActivity {
    RecyclerView rcvcatelogcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catelog);
        rcvcatelogcat=findViewById(R.id.rcvcatelogcat);
    }
}