package com.example.riteshapi.SubCategory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.CommonSubCategoryResponce;
import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubCategoryActivity extends AppCompatActivity {

    EditText subsearchbar;
    AppCompatSpinner subspinnerDropdown;
    RecyclerView subrcv;
    Button subbtnpluse;
    SwipeRefreshLayout subswap;
    RestCall restCall;

    int selectedPos = 0;
    CardView crs;

    SubCategoryAdapter subCategoryAdapter;
    String selectedCategoryId;
    String selectedCategoryName;
    SharedPreference sharedPreference;
    List<CategoryListResponce.Category> categoryListResponceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subbtnpluse=findViewById(R.id.subbtnpluse);
        subrcv=findViewById(R.id.subrcv);

        subsearchbar=findViewById(R.id.subsearchbar);
        crs=findViewById(R.id.crs);
        crs.setVisibility(View.GONE);

        subswap=findViewById(R.id.subswap);
        subspinnerDropdown=findViewById(R.id.subspinnerDropdown);
        sharedPreference=new SharedPreference(this);

        /*String[] st=new String[categoryListResponceList.get];*/

        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        subrcv.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this));
        subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, new ArrayList<>());
        GetCatogary();

        subswap.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetSubCategory();
            }
        });

        subbtnpluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubCategoryActivity.this, SubCategoryAddActivity.class);
                startActivity(intent);
            }
        });

        subsearchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subCategoryAdapter.searchdata(charSequence,subrcv);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    public void GetSubCategory() {
        subswap.setRefreshing(false);
        if (selectedCategoryId == null || selectedCategoryId.equals("-1")) {
            // No category selected, update the RecyclerView with an empty list
            subCategoryAdapter.updateData(new ArrayList<>());
        } else {
            restCall.getSubCategory("getSubCategory", selectedCategoryId,sharedPreference.getStringvalue("user_id"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SubCategoryListResponce>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("##", e.getLocalizedMessage());
                                    Toast.makeText(SubCategoryActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        @Override
                        public void onNext(SubCategoryListResponce subCategoryListResponce) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (subCategoryListResponce != null
                                            && subCategoryListResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)
                                            && subCategoryListResponce.getSubCategoryList() != null
                                            && subCategoryListResponce.getSubCategoryList().size() > 0) {
                                        subCategoryAdapter.updateData(subCategoryListResponce.getSubCategoryList());

                                        subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, new ArrayList<>());

                                        LinearLayoutManager layoutManager = new LinearLayoutManager(SubCategoryActivity.this);
                                        subrcv.setLayoutManager(layoutManager);
                                        subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this,
                                                subCategoryListResponce.getSubCategoryList());
                                        subrcv.setAdapter(subCategoryAdapter);
                                        subCategoryAdapter.SetUpInterface(new SubCategoryAdapter.SubCategoryClick() {
                                            @Override
                                            public void SubEditClick(SubCategoryListResponce.SubCategory subCategory) {
                                                String categoryId = subCategory.getCategoryId();
                                                String selectedSubCategoryId = subCategory.getSubCategoryId();
                                                String subCategoryName = subCategory.getSubcategoryName();

                                                Intent i = new Intent(SubCategoryActivity.this, SubCategoryAddActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("category_id", categoryId);
                                                bundle.putString("sub_category_id", selectedSubCategoryId);
                                                bundle.putString("subCategoryName", subCategoryName);
                                                i.putExtras(bundle);
                                                startActivity(i);
                                            }

                                            @Override
                                            public void SubDeleteClick(SubCategoryListResponce.SubCategory subCategory) {
                                                String subCategoryId = subCategory.getSubCategoryId();

                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubCategoryActivity.this);
                                                alertDialog.setTitle("Alert!!");
                                                alertDialog.setMessage("Are you sure, you want to delete Sub Category " + subCategory.getSubcategoryName());

                                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        deleteSubCategoryCall(subCategoryId);
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
                                        });


                                    } else {

                                        subCategoryAdapter.updateData(new ArrayList<>());
                                    }
                                }
                            });

                        }
                    });

        }
    }
    public void GetCatogary(){

        restCall.getCategory("getCategory",sharedPreference.getStringvalue("user_id"))
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
                                Toast.makeText(SubCategoryActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    @Override
                    public void onNext(CategoryListResponce categoryListResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryListResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)&&categoryListResponce.getCategoryList()!=null
                                        &&categoryListResponce.getCategoryList().size()>0){

                                    List<CategoryListResponce.Category> activeCateGories= categoryListResponce.getCategoryList();
                                    String[] categoryNameArray = new String[activeCateGories.size() + 1];
                                    String[] categoryIdArray = new String[activeCateGories.size() + 1];

                                    categoryNameArray[0] = "Select Category";
                                    categoryIdArray[0] = "-1";

                                    for (int i = 0; i < activeCateGories.size(); i++) {

                                        categoryNameArray[i + 1] = activeCateGories.get(i).getCategoryName();
                                        categoryIdArray[i + 1] = activeCateGories.get(i).getCategoryId();
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SubCategoryActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, categoryNameArray);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subspinnerDropdown.setAdapter(arrayAdapter);

                                    subspinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            selectedPos = position;
                                            if (selectedPos >= 0 && selectedPos < categoryIdArray.length) {
                                                selectedCategoryId = categoryIdArray[selectedPos];
                                                selectedCategoryName = categoryNameArray[selectedPos];

                                                if (selectedCategoryId.equals("-1")) {
                                                    // The "Select Category" option is selected, hide the search bar
                                                    crs.setVisibility(View.GONE);
                                                } else {
                                                    // A category is selected, show the search bar
                                                    crs.setVisibility(View.VISIBLE);
                                                    GetSubCategory();
                                                }

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }
                                subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, new ArrayList<>());



                            }
                        });
                    }
                });
    }
    private void deleteSubCategoryCall(String subCategoryId) {
        restCall.deleteSubCategory("DeleteSubCategory", subCategoryId,sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CommonSubCategoryResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SubCategoryActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CommonSubCategoryResponce subCategoryListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (subCategoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    GetSubCategory();
                                }
                                Toast.makeText(SubCategoryActivity.this, subCategoryListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }


}