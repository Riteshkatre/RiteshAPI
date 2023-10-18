package com.example.riteshapi.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.riteshapi.Tools;
import com.example.riteshapi.Variablebags.VeriableBag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ProductAct extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    AppCompatSpinner spinner_Category, spinner_SubCategory;
    RecyclerView rvProduct;
    RestCall restCall;
    EditText searchbar;

    ProductAdapter productAdapter;
    SharedPreference preferenceManger;
    String user_id, category_Id, subCat_id, product_id;
    Tools tools;

    List<String> sub_Category_name, sub_Category_id, category_id, categoryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        floatingActionButton = findViewById(R.id.btn_product_float);
        spinner_Category = findViewById(R.id.spinner_category);
        spinner_SubCategory = findViewById(R.id.spinner_subcategory);
        rvProduct = findViewById(R.id.rv_product_data);
        searchbar=findViewById(R.id.searchbar);

        sub_Category_name = new ArrayList<>();
        sub_Category_id = new ArrayList<>();
        category_id = new ArrayList<>();
        categoryNames = new ArrayList<>();
        preferenceManger = new SharedPreference(this);


        user_id = preferenceManger.getStringvalue("user_id");


        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAct.this, AddProductActivity.class);
                intent.putExtra("category_Id", category_Id);
                intent.putExtra("subCat_id", subCat_id);
                startActivity(intent);
            }

        });

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productAdapter.Search(charSequence,rvProduct);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (category_Id == null){
            getCategories();
        }
        else {
            GetProduct(subCat_id);
        }

    }


    private void getCategories() {
        restCall.getCategory("getCategory", user_id)
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
                                Toast.makeText(ProductAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CategoryListResponce categoryListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (categoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) &&
                                        categoryListResponse.getCategoryList() != null &&
                                        categoryListResponse.getCategoryList().size() > 0) {

                                    categoryNames.clear();
                                    category_id.clear();
                                    categoryNames.add("Select Category");

                                    for (int i = 0; i < categoryListResponse.getCategoryList().size(); i++) {
                                        categoryNames.add(categoryListResponse.getCategoryList().get(i).getCategoryName());
                                    }


                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductAct.this,
                                            android.R.layout.simple_spinner_dropdown_item, categoryNames);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_Category.setAdapter(arrayAdapter);

                                    spinner_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                                            for (int i = 0; i < categoryListResponse.getCategoryList().size(); i++) {

                                                if (categoryListResponse.getCategoryList().get(i).getCategoryName() == adapterView.getSelectedItem()) {

                                                    category_Id = categoryListResponse.getCategoryList().get(i).getCategoryId();
                                                    getSub_Category(category_Id);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });

                                } else {
                                    Toast.makeText(ProductAct.this, "No Categories Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    public void getSub_Category(String categoryId) {

        restCall.getSubCategory("getSubCategory", categoryId, user_id)
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
                                Toast.makeText(ProductAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNext(SubCategoryListResponce subCategoryListResponse) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("APIDEBUG", "Status: " + subCategoryListResponse.getMessage());

                                if (subCategoryListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) &&
                                        subCategoryListResponse.getSubCategoryList() != null &&
                                        subCategoryListResponse.getSubCategoryList().size() > 0) {

                                    sub_Category_name.clear();
                                    sub_Category_id.clear();
                                    sub_Category_name.add("Select SubCategory");

                                    for (int i = 0; i < subCategoryListResponse.getSubCategoryList().size(); i++) {
                                        sub_Category_name.add(subCategoryListResponse.getSubCategoryList().get(i).getSubcategoryName());
                                    }


                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductAct.this,
                                            android.R.layout.simple_spinner_dropdown_item, sub_Category_name);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_SubCategory.setAdapter(arrayAdapter);

                                    spinner_SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            for (int i = 0; i < subCategoryListResponse.getSubCategoryList().size(); i++) {

                                                if (subCategoryListResponse.getSubCategoryList().get(i).getSubcategoryName() == adapterView.getSelectedItem()) {

                                                    subCat_id = subCategoryListResponse.getSubCategoryList().get(i).getSubCategoryId();
                                                    GetProduct(subCat_id);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(ProductAct.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });
    }


    public void GetProduct(String subCategoryID) {

        restCall.GetProduct("getProduct", category_Id, subCategoryID, user_id)
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
                                Toast.makeText(ProductAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(ProductListResponce productListResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (productListResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) &&
                                        productListResponse.getProductList() != null &&
                                        productListResponse.getProductList().size() > 0) {

                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductAct.this);
                                    productAdapter = new ProductAdapter(ProductAct.this, productListResponse.getProductList());
                                    rvProduct.setLayoutManager(layoutManager);
                                    rvProduct.setAdapter(productAdapter);


                                    productAdapter.setUpItemClickListener(new ProductAdapter.onProductItemClickListener() {
                                        @Override
                                        public void onProductItemClick(ProductListResponce.Product product) {

                                            Intent intent = new Intent(ProductAct.this, AddProductActivity.class);
                                            intent.putExtra("category_Id", category_Id);
                                            intent.putExtra("subCat_id", subCat_id);
                                            intent.putExtra("product_Id", product.getProductId());
                                            intent.putExtra("getProductName", product.getProductName());
                                            intent.putExtra("getProductPrice", product.getProductPrice());
                                            intent.putExtra("getProductDesc", product.getProductDesc());
                                            intent.putExtra("getOldProductImage", product.getOldProductImage());
                                            intent.putExtra("getIsVeg", product.getIsVeg());
                                            intent.putExtra("getProductImage", product.getProductImage());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onProductItemClickDelete(ProductListResponce.Product product) {
                                            product_id = product.getProductId();


                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductAct.this);
                                            alertDialog.setTitle("Alert!!");
                                            alertDialog.setMessage("Are you sure, you want to delete " + product_id +"->" + preferenceManger.getStringvalue("user_id"));
                                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    deleteProduct(product_id);

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
                                    Log.d("API_DEBUG", "No Data Found");
                                    Toast.makeText(ProductAct.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }
                });
    }


    public void deleteProduct(String productId) {

        restCall.DeleteProduct("DeleteProduct", productId, user_id)
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
                                Toast.makeText(ProductAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(CommonResponce commonResponse) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (commonResponse.getStatus().equals(VeriableBag.SUCCESS_CODE)) {
                                    getCategories();
                                    Toast.makeText(ProductAct.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductAct.this, "Not able to Delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }
}