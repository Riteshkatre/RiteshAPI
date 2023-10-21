package com.example.riteshapi.Catelog;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CatalogListResponse;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CatalogActivity extends AppCompatActivity {

    RecyclerView recyclerViewCatalog;
    RestCall restCall;
   SharedPreference preferenceManager;


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        recyclerViewCatalog = findViewById(R.id.recyclerViewCatalog);
        preferenceManager = new SharedPreference(this);

        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);

        getCatalog();
    }


    void getCatalog(){
        restCall.GetCatalog("GetCatalog",preferenceManager.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CatalogListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(CatalogActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CatalogListResponse catalogListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (catalogListResponse.getCategoryList() != null && catalogListResponse.getCategoryList().size() > 0 && catalogListResponse.getStatus().equals(VeriableBag.SUCCESS_CODE)){
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CatalogActivity.this);
                                    CatalogCategoryAdapter catalogCategoryAdapter = new CatalogCategoryAdapter(catalogListResponse.getCategoryList(),CatalogActivity.this);
                                    recyclerViewCatalog.setLayoutManager(layoutManager);
                                    recyclerViewCatalog.setAdapter(catalogCategoryAdapter);
                                }
                            }
                        });
                    }
                });

    }
}