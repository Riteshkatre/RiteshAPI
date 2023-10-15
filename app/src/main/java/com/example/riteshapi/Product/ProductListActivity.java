package com.example.riteshapi.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.NetworkResponce.ProductListResponce;
import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ProductListActivity extends AppCompatActivity {

    AppCompatSpinner categorySpinnerProduct,subCategorySpinnerProduct;
    EditText etvProductSearch;
    LinearLayout noDataFoundView;
    RecyclerView productRecyclerView;
    FloatingActionButton btnAddProduct;
    RestCall restCall;
  SharedPreference sharedPreference;
   ProductListAdapter productListAdapter;
    int selectedPos = 0;
    String selectedCategoryId, selectedCategoryName,selectedSubCategoryId, selectedSubCategoryName,product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        categorySpinnerProduct = findViewById(R.id.categorySpinnerProduct);
        subCategorySpinnerProduct = findViewById(R.id.subCategorySpinnerProduct);
        etvProductSearch = findViewById(R.id.etvProductSearch);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        noDataFoundView = findViewById(R.id.noDataFoundView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(new ArrayList<>(), ProductListActivity.this);
        productRecyclerView.setAdapter(productListAdapter);


        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);

        sharedPreference = new SharedPreference(this);

        getProductCateCall();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListActivity.this, ProductAddActivity.class));
            }
        });

        etvProductSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productListAdapter.Search(charSequence,productRecyclerView);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductCateCall();
    }




    private void getProductCateCall() {

        noDataFoundView.setVisibility(View.VISIBLE);


        restCall.getCategory("getCategory",sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CategoryListResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProductListActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onNext(CategoryListResponce categoryListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (categoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) && categoryListResponse.getCategoryList() != null
                                        && categoryListResponse.getCategoryList().size() > 0) {

                                    List<CategoryListResponce.Category> categories = categoryListResponse.getCategoryList();
                                    String[] categoryNameArray = new String[categories.size() + 1];
                                    String[] categoryIdArray = new String[categories.size() + 1];

                                    categoryNameArray[0] = "Select Category";
                                    categoryIdArray[0] = "-1";

                                    for (int i = 0; i < categories.size(); i++) {
                                        categoryNameArray[i + 1] = categories.get(i).getCategoryName();
                                        categoryIdArray[i + 1] = categories.get(i).getCategoryId();
                                    }

                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductListActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryNameArray);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    categorySpinnerProduct.setAdapter(arrayAdapter);

                                    categorySpinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            selectedPos = position;
                                            if (selectedPos >= 0 && selectedPos < categoryIdArray.length) {
                                                selectedCategoryId = categoryIdArray[selectedPos];
                                                selectedCategoryName = categoryNameArray[selectedPos];

                                                if (selectedCategoryId.equalsIgnoreCase("-1")) {
                                                    Toast.makeText(ProductListActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
//                                                    Toast.makeText(AddProductActivity.this, "SubCategory Loading", Toast.LENGTH_SHORT).show();
                                                    getProductSubCateCall();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                });
        productListAdapter = new ProductListAdapter(new ArrayList<>(),ProductListActivity.this);
    }

    private void getProductSubCateCall() {
        restCall.getSubCategory("getSubCategory",selectedCategoryId,sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<SubCategoryListResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProductListActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(SubCategoryListResponce subCategoryListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (subCategoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) && subCategoryListResponse.getSubCategoryList() != null
                                        && subCategoryListResponse.getSubCategoryList().size() > 0) {

                                    List<SubCategoryListResponce.SubCategory> subCategories = subCategoryListResponse.getSubCategoryList();
                                    String[] subCategoryNameArray = new String[subCategories.size() + 1];
                                    String[] subCategoryIdArray = new String[subCategories.size() + 1];

                                    subCategoryNameArray[0] = "Select Sub Category";
                                    subCategoryIdArray[0] = "-1";

                                    for (int i = 0; i < subCategories.size(); i++) {
                                        subCategoryNameArray[i + 1] = subCategories.get(i).getSubcategoryName();
                                        subCategoryIdArray[i + 1] = subCategories.get(i).getSubCategoryId();
                                    }

                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductListActivity.this, android.R.layout.simple_spinner_dropdown_item, subCategoryNameArray);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subCategorySpinnerProduct.setAdapter(arrayAdapter);

                                    subCategorySpinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            selectedPos = position;
                                            if (selectedPos >= 0 && selectedPos < subCategoryIdArray.length) {
                                                selectedSubCategoryId = subCategoryIdArray[selectedPos];
                                                selectedSubCategoryName = subCategoryNameArray[selectedPos];

//                                                Toast.makeText(AddProductActivity.this, ""+selectedSubCategoryId, Toast.LENGTH_SHORT).show();

                                                if (selectedCategoryId.equalsIgnoreCase("-1") && selectedSubCategoryId.equalsIgnoreCase("-1")) {
                                                    Toast.makeText(ProductListActivity.this, "Select Sub Category", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    getProduct();
//                                                    Toast.makeText(AddProductActivity.this, "SubCategory Loading", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                            }
                        });
                    }
                });
        productListAdapter = new ProductListAdapter( new ArrayList<>(),ProductListActivity.this);
    }

    private void getProduct(){
        if (selectedCategoryId.equalsIgnoreCase("-1") || selectedSubCategoryId.equalsIgnoreCase("-1")) {
            productRecyclerView.setVisibility(View.GONE);
            noDataFoundView.setVisibility(View.VISIBLE);
        } else {
            productRecyclerView.setVisibility(View.VISIBLE);
            noDataFoundView.setVisibility(View.GONE);

        }
        restCall.getProduct("getProduct",selectedCategoryId,selectedSubCategoryId,sharedPreference.getStringvalue("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ProductListResponce>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProductListActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(ProductListResponce productListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (productListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)
                                        && productListResponse.getProductList() != null && productListResponse.getProductList().size() > 0){

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ProductListActivity.this);
                                    productRecyclerView.setLayoutManager(layoutManager);

                                    productListAdapter = new ProductListAdapter(productListResponse.getProductList(),ProductListActivity.this);
                                    productRecyclerView.setAdapter(productListAdapter);
                                    productListAdapter.SetUpInterface(new ProductListAdapter.ProductClick() {
                                        @Override
                                        public void DeleteClick(ProductListResponce.Product product) {
                                            DataDeleteClick(product);
                                        }
                                    });

                                }
                            }
                        });
                    }
                });
    }
    public void DeleteProductCall() {
        restCall.deleteProduct("DeleteProduct", product_id, sharedPreference.getStringvalue("user_id"))
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
                                Toast.makeText(ProductListActivity.this, "Error while deleting the product", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNext(CommonResponce commonResponce) {
                        if (commonResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)){
                            Toast.makeText(ProductListActivity.this,"product deleted ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductListActivity.this, "Faild to delete" + commonResponce.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public void DataDeleteClick(ProductListResponce.Product product) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductListActivity.this);
        alertDialog.setTitle("Alert!!");
        alertDialog.setMessage("Are you sure, you want to delete " + product.getProductId() + sharedPreference.getStringvalue("user_id"));
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteProductCall();
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




}
