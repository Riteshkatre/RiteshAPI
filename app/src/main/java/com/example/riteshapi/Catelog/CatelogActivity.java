package com.example.riteshapi.Catelog;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.riteshapi.Catelog.NetworkResponce.CatalogueListResponse;
import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CatelogActivity extends AppCompatActivity {
    RecyclerView rcvcatelogcat;
    RestCall restCall;
    TextView txtNoDataAvailable;
    SwipeRefreshLayout swipe;




    SharedPreference sharedPreference;
    CatalogueAdapter catalogueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catelog);
        rcvcatelogcat=findViewById(R.id.rcvcatelogcat);
        txtNoDataAvailable=findViewById(R.id.txtNoDataAvailable);
        swipe=findViewById(R.id.swipe);
        init();
    }


    private void init() {

       txtNoDataAvailable.setVisibility(View.VISIBLE);
        rcvcatelogcat.setVisibility(View.GONE);
        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        sharedPreference = new SharedPreference(CatelogActivity.this);



        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCatalogue();
                    }
                }, 3000);
            }
        });
    }

    private void getCatalogue() {
        restCall.GetCatalog("GetCatalog", sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // Observe on the main (UI) thread
                .subscribe(new Subscriber<CatalogueListResponse>() {
                    @Override
                    public void onCompleted() {
                        // You can perform any cleanup or post-processing here.
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle the error, and show a toast message for now.
                        // You can add more specific error handling based on your requirements.
                        txtNoDataAvailable.setVisibility(View.VISIBLE);
                        rcvcatelogcat.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                        Toast.makeText(CatelogActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CatalogueListResponse catalogueListResponse) {
                        // Handle the response here, update the UI as needed.
                        if (catalogueListResponse != null && catalogueListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                            txtNoDataAvailable.setVisibility(View.GONE);
                            rcvcatelogcat.setVisibility(View.VISIBLE);
                            swipe.setRefreshing(false);
                            setupRecyclerView(catalogueListResponse);
                        } else {
                            // Handle the case where the response is not successful.
                            txtNoDataAvailable.setVisibility(View.VISIBLE);
                            rcvcatelogcat.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void setupRecyclerView(CatalogueListResponse catalogueListResponse) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvcatelogcat.setLayoutManager(layoutManager);

        catalogueAdapter = new CatalogueAdapter(this, catalogueListResponse.getCategoryList());
        rcvcatelogcat.setAdapter(catalogueAdapter);
    }}