package com.example.riteshapi.Product;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Tools;
import com.example.riteshapi.Variablebags.VeriableBag;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProductAddActivity extends AppCompatActivity {
    ImageView picimage, editimage;
    Button btncancel, btnsubmit;
    TextInputEditText etproductname, etprice, description;
    SwitchCompat btnswitch;
    ActivityResultLauncher<Intent> cameraLauncher = null;
    SharedPreference sharedPreference;
    AppCompatSpinner categoryspiner, subcategoryspinner;
    RestCall restCall;
    private File currentPhotoFile;

    String currentPhotoPath = "";
    Tools tools;
    int selectedCategoryPos = 0;
    int selectedsubPos = 0;
    String selectedCategoryId, selectedSubCategoryId, selectedCategoryName, selectedsubCategoryName;

    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        picimage = findViewById(R.id.picimage);
        editimage = findViewById(R.id.editimage);
        btncancel = findViewById(R.id.btncancel);
        btnsubmit = findViewById(R.id.btnsubmit);
        etproductname = findViewById(R.id.etproductname);
        etprice = findViewById(R.id.etprice);
        description = findViewById(R.id.description);
        btnswitch = findViewById(R.id.btnswitch);
        subcategoryspinner = findViewById(R.id.subcategoryspinner);
        categoryspiner = findViewById(R.id.categoryspiner);
        sharedPreference = new SharedPreference(ProductAddActivity.this);

        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        GetCatogary();


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Camera capture was successful, handle the result.
                Tools.displayImage(ProductAddActivity.this,currentPhotoPath,picimage);
            } else {
                Toast.makeText(this, "Not", Toast.LENGTH_SHORT).show();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    currentPhotoPath = "";
                    if (checkCameraPermission()) {
                        openCamera();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.riteshapi",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraLauncher.launch(takePictureIntent);
            }
        }

    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoFile=image;
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void AddProduct() {

        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "AddProduct");
        RequestBody rbCategoryId = RequestBody.create(MediaType.parse("text/plain"), selectedCategoryId);
        RequestBody rbSubCategoryId = RequestBody.create(MediaType.parse("text/plain"), selectedSubCategoryId);
        RequestBody rbProductName = RequestBody.create(MediaType.parse("text/plain"), etproductname.getText().toString().trim());
        RequestBody rbProductPrice = RequestBody.create(MediaType.parse("text/plain"), etprice.getText().toString().trim());
        RequestBody rbProductDesc = RequestBody.create(MediaType.parse("text/plain"), description.getText().toString().trim());
        RequestBody rbIsVeg = RequestBody.create(MediaType.parse("text/plain"), btnswitch.isChecked() ? "1" : "0");
        RequestBody rbUserId = RequestBody.create(MediaType.parse("text/plain"), sharedPreference.getStringvalue("user_id"));
        MultipartBody.Part fileToUploadfile = null;

        if (fileToUploadfile == null && currentPhotoPath != "") {
            try {
                StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder2.build());
                    File file = new File(currentPhotoPath);
                RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), file);
                fileToUploadfile = MultipartBody.Part.createFormData("product_image", file.getName(), requestBody);

            } catch (Exception e) {
                Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


        restCall.AddProduct(tag, rbCategoryId, rbSubCategoryId, rbProductName, fileToUploadfile, rbProductPrice, rbProductDesc, rbIsVeg, rbUserId)
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
                                    Toast.makeText(ProductAddActivity.this, "Error while adding product", Toast.LENGTH_SHORT).show();
                                    Log.e("##", "run: " + e.getLocalizedMessage());
                                }
                            });
                        }

                        @Override
                        public void onNext(CommonResponce commonResponce) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ProductAddActivity.this, "" + commonResponce.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (commonResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                        if (currentPhotoFile != null && currentPhotoPath != null) {
                                            currentPhotoFile.delete();
                                        }
                                        finish();
                                    }
                                }
                            });
                        }
                    });

    }


    public void GetCatogary() {

        restCall.getCategory("getCategory", sharedPreference.getStringvalue("user_id"))
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
                                Log.e("##", e.getLocalizedMessage());
                                Toast.makeText(ProductAddActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNext(CategoryListResponce categoryListResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (categoryListResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE) && categoryListResponce.getCategoryList() != null
                                        && categoryListResponce.getCategoryList().size() > 0) {

                                    List<CategoryListResponce.Category> activeCateGories = categoryListResponce.getCategoryList();
                                    String[] categoryNameArray = new String[activeCateGories.size() + 1];
                                    String[] categoryIdArray = new String[activeCateGories.size() + 1];

                                    categoryNameArray[0] = "Select Category";
                                    categoryIdArray[0] = "-1";

                                    for (int i = 0; i < activeCateGories.size(); i++) {

                                        categoryNameArray[i + 1] = activeCateGories.get(i).getCategoryName();
                                        categoryIdArray[i + 1] = activeCateGories.get(i).getCategoryId();
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductAddActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, categoryNameArray);

                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    categoryspiner.setAdapter(arrayAdapter);

                                    categoryspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                            selectedCategoryPos = position;
                                            if (selectedCategoryPos >= 0 && selectedCategoryPos < categoryIdArray.length) {
                                                selectedCategoryId = categoryIdArray[selectedCategoryPos];
                                                selectedCategoryName = categoryNameArray[selectedCategoryPos];
                                                GetSubCategoryApi(selectedCategoryId);

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }
                                Toast.makeText(ProductAddActivity.this, "" + categoryListResponce.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        });
                    }
                });
    }

    public void GetSubCategoryApi(String selectedCategoryId) {

        restCall.getSubCategory("getSubCategory", selectedCategoryId, sharedPreference.getStringvalue("user_id"))
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
                                Log.e("##", e.getLocalizedMessage());
                                Toast.makeText(ProductAddActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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

                                    List<SubCategoryListResponce.SubCategory> subactiveCateGories = subCategoryListResponce.getSubCategoryList();
                                    String[] subcategoryNameArray = new String[subactiveCateGories.size() + 1];
                                    String[] subcategoryIdArray = new String[subactiveCateGories.size() + 1];

                                    subcategoryNameArray[0] = "Select SubCategory";
                                    subcategoryIdArray[0] = "-1";
                                    for (int i = 0; i < subactiveCateGories.size(); i++) {

                                        subcategoryNameArray[i + 1] = subactiveCateGories.get(i).getSubcategoryName();
                                        subcategoryIdArray[i + 1] = subactiveCateGories.get(i).getSubCategoryId();
                                    }
                                    ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(ProductAddActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, subcategoryNameArray);

                                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subcategoryspinner.setAdapter(arrayAdapter2);
                                    subcategoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int subposition, long id) {
                                            selectedsubPos = subposition;
                                            if (selectedsubPos >= 0 && selectedsubPos < subcategoryIdArray.length) {
                                                selectedSubCategoryId = subcategoryIdArray[selectedsubPos];
                                                selectedsubCategoryName = subcategoryNameArray[selectedsubPos];


                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });


                                } else {
                                    Toast.makeText(ProductAddActivity.this, "" + subCategoryListResponce.getMessage(), Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

                    }
                });

    }


    public  void  EditProduct(String categoryId,String subCategoryId,String productId,String productName,
                              String productPrice,String oldImageProduct,String productDesc,String isVeg,String productImage){

        restCall.EditProduct("EditProduct",categoryId,subCategoryId,productId,productName,productPrice,oldImageProduct,productDesc,isVeg,productImage,sharedPreference.getStringvalue("user_id"))
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

                            }
                        });

                    }

                    @Override
                    public void onNext(CommonResponce commonResponce) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }
                });
    }


}