package com.example.riteshapi.Category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    EditText etv;
    Button btnadd;
    RestCall restCall;
    RecyclerView rcv;
    SharedPreference sharedPreference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etv = findViewById(R.id.etv);
        btnadd = findViewById(R.id.btnadd);
        rcv = findViewById(R.id.rcv);
        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        sharedPreference=new SharedPreference(this);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etv.getText().toString().trim().equalsIgnoreCase("")) {
                    etv.setError("please add category name");
                    etv.requestFocus();
                } else {
                    Addcategory(etv.getText().toString().trim());
                }
            }
        });

    }
    public void Addcategory(String categoryName) {
        restCall.AddCategory("AddCategory",sharedPreference.getStringvalue("user_id"),categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonResponce>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "NO CONNECTION", Toast.LENGTH_SHORT).show();
                                Log.e("##", "run: " + e.getLocalizedMessage());
                            }
                        });

                    }

                    @Override
                    public void onNext(CommonResponce commonResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    etv.setText("");

                                    finish();
                                }
                                Toast.makeText(MainActivity.this, commonResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }



}
