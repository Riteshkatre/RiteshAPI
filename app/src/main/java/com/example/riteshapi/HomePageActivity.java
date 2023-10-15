package com.example.riteshapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riteshapi.Product.ProductAddActivity;
import com.example.riteshapi.Product.ProductListActivity;
import com.example.riteshapi.RegistrationAndSlash.RegistrationActivity;
import com.example.riteshapi.RegistrationAndSlash.SharedPreference;
import com.example.riteshapi.SubCategory.SubCategoryActivity;

import com.example.riteshapi.Category.ResultActivity;

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

        userNameTextView.setText(sharedPreference.getStringvalue("first_name")+".......");


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
                Intent intent = new Intent(HomePageActivity.this, ProductAddActivity.class);
                startActivity(intent);


            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ProductListActivity.class);
                startActivity(intent);

            }
        });
    }
}