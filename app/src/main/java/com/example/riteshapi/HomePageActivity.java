package com.example.riteshapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riteshapi.Category.ResultActivity;
import com.example.riteshapi.Catelog.CatalogActivity;
import com.example.riteshapi.Product.ProductAct;
import com.example.riteshapi.RegistrationAndSlash.RegistrationActivity;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.SubCategory.SubCategoryActivity;

public class HomePageActivity extends AppCompatActivity {
    Button category, subcategory,productadd,productlist;
    TextView userNameTextView,logout;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        category = findViewById(R.id.category);
        subcategory = findViewById(R.id.subcategory);
        productadd = findViewById(R.id.productadd);
        productlist=findViewById(R.id.productlist);
        logout=findViewById(R.id.logout);

        sharedPreference = new SharedPreference(HomePageActivity.this);

        userNameTextView = findViewById(R.id.userNameTextView);

        userNameTextView.setText(sharedPreference.getStringvalue("first_name")+"...");


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
        subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, SubCategoryActivity.class);
                startActivity(intent);

            }
        });
        productadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, CatalogActivity.class);
                startActivity(intent);


            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HomePageActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Clear or reset user-related preferences
                                sharedPreference.setLoggedIn(false);
                                sharedPreference.setUserId("");

                                // Redirect to the login screen
                                Intent intent = new Intent(HomePageActivity.this, RegistrationActivity.class);
                                // Set the FLAG_ACTIVITY_NEW_TASK and FLAG_ACTIVITY_CLEAR_TASK flags to clear the back stack
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ProductAct.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Alert !!")
                .setMessage("Are you sure you want to exit this App ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("CANCEL",null)
                .show();
    }
}