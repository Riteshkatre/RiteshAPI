package com.example.riteshapi.SubCategory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.CommonSubCategoryResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubCategoryAddActivity extends AppCompatActivity {
    RestCall restCall;
    EditText edtsubname;
    Button btnaddsubdata;
    AppCompatSpinner spinneradd;

    String selectedCategoryId,selectedSubCategoryId,selectedCategoryName;
    SharedPreference sharedPreference;

    int selectedPos = 0;
    boolean isEdit ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_add);
        edtsubname=findViewById(R.id.edtsubname);
        btnaddsubdata=findViewById(R.id.btnaddsubdata);
        spinneradd=findViewById(R.id.spinneradd);
        sharedPreference=new SharedPreference(this);

        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);

        GetCatogary();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("category_id") != null) {

            isEdit = true;
            selectedCategoryId = bundle.getString("category_id");
            selectedSubCategoryId = bundle.getString("sub_category_id");
            selectedCategoryName = bundle.getString("subCategoryName");

            edtsubname.setText(selectedCategoryName);
            btnaddsubdata.setText("Edit");
        } else {
            isEdit = false;
            btnaddsubdata.setText("Add");
        }

        btnaddsubdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCategoryId == null || selectedCategoryId.equals("-1")) {
                    // No valid category selected
                    Toast.makeText(SubCategoryAddActivity.this, "Please select a category.", Toast.LENGTH_SHORT).show();
                } else if (edtsubname != null && edtsubname.getText().toString().trim().isEmpty()) {
                    edtsubname.setError("Enter Sub-Category Name");
                    edtsubname.requestFocus();
                } else {
                    if (isEdit) {
                        editSubCategoryCall();
                    } else {
                        AddSubCategory(selectedCategoryId, edtsubname.getText().toString().trim());
                    }
                }
            }
        });


    }

    public void AddSubCategory(String category_id,String subcategory_name) {
        restCall.AddSubCategory("AddSubCategory",category_id,subcategory_name,sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io()).
                observeOn(Schedulers.newThread()).
                subscribe(new Subscriber<CommonSubCategoryResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("API Error", e.getMessage());
                                Toast.makeText(SubCategoryAddActivity.this, "This is  wrong...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CommonSubCategoryResponce commonSubCategoryResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonSubCategoryResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    edtsubname.setText("");
                                    finish();

                                }
                                Toast.makeText(SubCategoryAddActivity.this, ""+commonSubCategoryResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                });
    }
    private void editSubCategoryCall() {
        restCall.editSubCategory("EditSubCategory", selectedCategoryId,
                        edtsubname.getText().toString(), selectedSubCategoryId,
                        sharedPreference.getStringvalue("user_id"))
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
                                Toast.makeText(SubCategoryAddActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CommonSubCategoryResponce subCategoryListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SubCategoryAddActivity.this, "Select Category to Edit", Toast.LENGTH_SHORT).show();
                                if (subCategoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    edtsubname.setText("");

                                    startActivity(new Intent(SubCategoryAddActivity.this, SubCategoryActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SubCategoryAddActivity.this, subCategoryListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
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
                                Toast.makeText(SubCategoryAddActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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

                                    List<CategoryListResponce.Category> activeCateGories=categoryListResponce.getCategoryList();
                                    String[] categoryNameArray = new String[activeCateGories.size() + 1];
                                    String[] categoryIdArray = new String[activeCateGories.size() + 1];

                                    categoryNameArray[0] = "Select Category";
                                    categoryIdArray[0] = "-1";

                                    for (int i = 0; i < activeCateGories.size(); i++) {

                                        categoryNameArray[i + 1] = activeCateGories.get(i).getCategoryName();
                                        categoryIdArray[i + 1] = activeCateGories.get(i).getCategoryId();
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SubCategoryAddActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, categoryNameArray);

                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinneradd.setAdapter(arrayAdapter);

                                    spinneradd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            selectedPos = position;
                                            if (selectedPos >= 0 && selectedPos < categoryIdArray.length) {
                                                selectedCategoryId = categoryIdArray[selectedPos];

                                                /*selectedCategoryName = categoryNameArray[selectedPos];*/

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {



                                        }
                                    });

                                }
                                Toast.makeText(SubCategoryAddActivity.this, ""+categoryListResponce.getMessage(), Toast.LENGTH_SHORT).show();




                            }
                        });
                    }
                });
    }
}