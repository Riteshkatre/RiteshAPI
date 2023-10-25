package com.example.riteshapi.Category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.riteshapi.Category.Adapter.RcvAdapter;
import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResultActivity extends AppCompatActivity {

    EditText searchbar;
    RecyclerView rcv;
    Button btnpluse;
    RcvAdapter rcvadapter;
    RestCall restCall2;
    RestCall restCall;
    ImageView cross,imgSearch;
    SwipeRefreshLayout swap;
    SharedPreference sharedPreference;
    RcvAdapter.CategoryClick categoryClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        searchbar=findViewById(R.id.searchbar);
        rcv=findViewById(R.id.rcv);
        btnpluse=findViewById(R.id.btnpluse);
        swap=findViewById(R.id.swap);
        cross=findViewById(R.id.cross);
        cross.setVisibility(View.GONE);
        imgSearch=findViewById(R.id.imgSearch);
        imgSearch.setVisibility(View.VISIBLE);
        rcv.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        rcvadapter = new RcvAdapter(ResultActivity.this,new ArrayList<>());
        rcv.setAdapter(rcvadapter);
        restCall2 = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        restCall=  RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        sharedPreference=new SharedPreference(this);

        searchbar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchbar.setHint(null);
                    imgSearch.setVisibility(View.GONE);
                } else {
                    searchbar.setHint("Search Name Here");
                    imgSearch.setVisibility(View.VISIBLE);
                }
            }
        });



        swap.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetCatogary();
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchbar.setText(""); // Clear the EditText text
                cross.setVisibility(View.GONE); // Hide the cross button
            }
        });





        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rcvadapter.Search(charSequence,rcv);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    cross.setVisibility(View.GONE);
                } else {
                    cross.setVisibility(View.VISIBLE);
                   // Show the cross button when there's text
                }

            }
        });


        btnpluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCatogary();
    }

    public void GetCatogary(){
        if (restCall2 == null) {
            Toast.makeText(ResultActivity.this, "RestCall is not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        restCall2.getCategory("getCategory",sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryListResponce>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("##",e.getLocalizedMessage());
                                Toast.makeText(ResultActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                                   }
                                   @Override
                    public void onNext(CategoryListResponce categoryListResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swap.setRefreshing(false);
                                if (categoryListResponce != null
                                        && categoryListResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)
                                        && categoryListResponce.getCategoryList() != null
                                        && categoryListResponce.getCategoryList().size() > 0) {
                                    rcvadapter = new RcvAdapter(ResultActivity.this,new ArrayList<>());

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ResultActivity.this);
                                    rcv.setLayoutManager(layoutManager);
                                    rcvadapter = new RcvAdapter(ResultActivity.this,categoryListResponce.getCategoryList());
                                    rcv.setAdapter(rcvadapter);

                                    rcvadapter.SetUpInterface(new RcvAdapter.CategoryClick() {
                                        @Override
                                        public void DeleteClick(CategoryListResponce.Category category) {
                                            DataDeleteClick(category);
                                        }
                                        @Override
                                        public void onSwitchChanged(CategoryListResponce.Category category, boolean isChecked) {
                                            String categoryId = category.getCategoryId();
                                            ActiveDeactiveCategoryCall(categoryId, isChecked);
                                        }

                                    });
                                } else {
                                    rcv.setVisibility(View.GONE);
                                    GetCatogary();

                                }
                            }
                        });
                    }
                });
    }

    public void DataDeleteClick(CategoryListResponce.Category category) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResultActivity.this);
        alertDialog.setTitle("Alert!!");
        alertDialog.setMessage("Are you sure, you want to delete " + category.getCategoryName()+category.getCategoryId());
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteCategoryCall(category.getCategoryId());
                dialogInterface.dismiss();

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
    public void DeleteCategoryCall(String category_id) {
        restCall.DeleteCategory("DeleteCategory",sharedPreference.getStringvalue("user_id"),category_id)
                .subscribeOn(Schedulers.io())
                .observeOn((Schedulers.newThread()))
                .subscribe(new Subscriber<CommonResponce>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResultActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onNext(CommonResponce commonResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    Toast.makeText(ResultActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    GetCatogary();
                                } else {
                                    Toast.makeText(ResultActivity.this, ""+commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

    }

    public void ActiveDeactiveCategoryCall(String categoryId, boolean isChecked) {
        String status = isChecked ? "0" : "1";
        restCall.ActiveDeactiveCategory("ActiveDeactiveCategory", status, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CommonResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResultActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CommonResponce categoryCommonResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (categoryCommonResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    Toast.makeText(ResultActivity.this, "Category Status Updated: " + status, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResultActivity.this, categoryCommonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }





}