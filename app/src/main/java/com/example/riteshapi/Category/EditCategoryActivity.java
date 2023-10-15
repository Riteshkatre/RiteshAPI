package com.example.riteshapi.Category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CommonResponce;
import com.example.riteshapi.R;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.Variablebags.VeriableBag;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class EditCategoryActivity extends AppCompatActivity {
    EditText etv;
    Button btnsave;
    RestCall restCall;
    SharedPreference sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        etv = findViewById(R.id.etv);
        btnsave = findViewById(R.id.btnsave);
        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        sharedPreference=new SharedPreference(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String categoryId = extras.getString("categoryId");
            String categoryName = extras.getString("categoryName");

            etv.setText(categoryName);

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newCategoryName = etv.getText().toString().trim();

                    EditCategory(categoryId, newCategoryName);
                }
            });
        }
    }
    private void EditCategory(String categoryId, String newCategoryName) {
        if (categoryId.isEmpty() || newCategoryName.isEmpty()) {
            Toast.makeText(this, "Category ID and new name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        restCall.EditCategory("EditCategory",newCategoryName, categoryId,sharedPreference.getStringvalue("user_id") )
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
                                Toast.makeText(EditCategoryActivity.this, "Error while editing category", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(EditCategoryActivity.this, "Category edited successfully", Toast.LENGTH_SHORT).show();
                                    etv.setText(""); // Clear the EditText
                                    Intent intent = new Intent();
                                    intent.putExtra("editedCategoryId", categoryId);
                                    intent.putExtra("editedCategoryName", newCategoryName);
                                    setResult(RESULT_OK, intent);
                                    finish();

                                } else {

                                    Toast.makeText(EditCategoryActivity.this, commonResponce.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }
}