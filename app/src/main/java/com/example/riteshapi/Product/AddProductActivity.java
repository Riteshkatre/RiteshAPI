package com.example.riteshapi.Product;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.schedulers.Schedulers;


public class AddProductActivity extends AppCompatActivity {

    ImageButton imageButtonEdit;
    ImageView imageView;
    String currentPhotoPath ="" , user_id, categoryID  , subCategoryID , imggg , productId ,
            productName , productPrice ,productDes ,productOldImage , productImage  , product_isVeg  ;
    RestCall restCall;
    ActivityResultLauncher<Intent> cameraLauncher;
    int REQUEST_CAMERA_PERMISSION = 101;
    File CurentPhotoFile ;
    TextInputEditText editTextName, editTextPrice, editTextDesc;
    Button btnSubmit ,buttonCancel;
    SharedPreference preferenceManger;
    boolean isEditMode ;




    Switch switchVegNonVeg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        imageButtonEdit = findViewById(R.id.img_edit);
        imageView = findViewById(R.id.im_img);
        editTextName = findViewById(R.id.txt_productName);
        editTextPrice = findViewById(R.id.txt_productPrice);
        editTextDesc = findViewById(R.id.txt_productDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        buttonCancel = findViewById(R.id.btnCancel);
        switchVegNonVeg = findViewById(R.id.switchbtn);
        preferenceManger= new SharedPreference(this);
        user_id = preferenceManger.getStringvalue("user_id");





        if (getIntent().getStringExtra("product_Id") != null){

            categoryID = getIntent().getStringExtra("category_Id");
            subCategoryID = getIntent().getStringExtra("subCat_id");
            productId = getIntent().getStringExtra("product_Id");
            productName = getIntent().getStringExtra("getProductName");
            productPrice = getIntent().getStringExtra("getProductPrice");
            productDes = getIntent().getStringExtra("getProductDesc");
            productOldImage = getIntent().getStringExtra("getOldProductImage");
            productImage = getIntent().getStringExtra("getProductImage");
            product_isVeg = getIntent().getStringExtra("getIsVeg");

            Glide .with(this)
                    .load(productImage)
                    .placeholder(R.drawable.ic_imageholder)
                    .into(imageView);
            btnSubmit.setText("EDIT");
            editTextName.setText(productName);
            editTextPrice.setText(productPrice);
            editTextDesc.setText(productDes);
            isEditMode = true;
        }else{
            isEditMode = false;
            btnSubmit.setText("Submit");
        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                categoryID = ((Intent) intent).getExtras().getString("category_Id");
                subCategoryID = ((Intent) intent).getExtras().getString("subCat_id");

                if(editTextName.getText().toString().equalsIgnoreCase("")){
                    editTextName.setError("Enter the name");
                    editTextName.requestFocus();
                } else if (editTextPrice.getText().toString().equalsIgnoreCase("")) {
                    editTextPrice.setError("Enter price");
                    editTextPrice.requestFocus();
                } else if (editTextDesc.getText().toString().equalsIgnoreCase("")) {
                    editTextDesc.setError("Enter the Description");
                    editTextDesc.requestFocus();
                }else {
                    restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);

                    if(isEditMode == true){
                        ProductEdit();

                    }else{
                        AddProduct();

                    }
                }
            }
        });
        imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    currentPhotoPath="";
                    if (checkCameraPermission()) {
                        openCamera();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Camera capture was successful, handle the result.
                displayImage(AddProductActivity.this,imageView,currentPhotoPath);
            }else {
                Toast.makeText(this, "Not", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AddProduct(){

        RequestBody requestBodyTag = RequestBody.create(MediaType.parse("text/plain"),"AddProduct");
        RequestBody requestBodyCategoryId = RequestBody.create(MediaType.parse("text/plain"),categoryID);
        RequestBody requestBodySubCategoryId = RequestBody.create(MediaType.parse("text/plain"),subCategoryID);
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("text/plain"),editTextName.getText().toString().trim());
        RequestBody requestBodyPrice = RequestBody.create(MediaType.parse("text/plain"),editTextPrice.getText().toString().trim());
        RequestBody requestBodyDesc = RequestBody.create(MediaType.parse("text/plain"),editTextDesc.getText().toString().trim());
        RequestBody requestBodyIsVeg = RequestBody.create(MediaType.parse("text/plain"),switchVegNonVeg.isChecked() ?"1":"0");
        RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("text/plain"),user_id);
        MultipartBody.Part fileToUploadfile = null;



        if (!currentPhotoPath.isEmpty()) {
            try {
                StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder2.build());
                File file = new File(currentPhotoPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                fileToUploadfile = MultipartBody.Part.createFormData("product_image", file.getName(), requestBody);
            } catch (Exception e) {
                Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }




        restCall.AddProduct(requestBodyTag,requestBodyCategoryId,requestBodySubCategoryId,requestBodyName,
                        requestBodyPrice,requestBodyDesc,requestBodyIsVeg,requestBodyUserId,fileToUploadfile)
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


                                Toast.makeText(AddProductActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onNext(CommonResponce commonResponse) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (commonResponse.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)){
                                    Toast.makeText(AddProductActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    editTextName.setText("");
                                    editTextPrice.setText("");
                                    editTextDesc.setText("");

                                    finish();
                                }else{
                                    Toast.makeText(AddProductActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }


    public  void ProductEdit(){
        productImage = currentPhotoPath;


        restCall.EditProduct("EditProduct",categoryID,subCategoryID,productId,editTextName.getText().toString(),
                        editTextPrice.getText().toString(),productOldImage,editTextDesc.getText().toString()
                        ,product_isVeg,user_id,productImage)
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


                                Toast.makeText(AddProductActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onNext(CommonResponce commonResponse) {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (commonResponse.getStatus().equals(VeriableBag.SUCCESS_CODE)){
                                    Toast.makeText(AddProductActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(AddProductActivity.this, "Not able to edit", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }



    private void displayImage(Context context, ImageView imageView, String currentPhotoPath) {

        Glide.with(context)

                .load(currentPhotoPath)
                .placeholder(R.drawable.ic_imageholder)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

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
        CurentPhotoFile=image;
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }}