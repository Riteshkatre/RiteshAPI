package com.example.riteshapi.RegistrationAndSlash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.example.riteshapi.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RegistrationActivity extends AppCompatActivity {

    TabLayout tablay;
    ViewPager2 viewPage2;
TextView tvb1,tvs1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Sign_inFragment signInFragment = new Sign_inFragment();
        Sign_upFragment signUpFragment = new Sign_upFragment();


        tvb1=findViewById(R.id.tvb1);
        tvs1=findViewById(R.id.tvs1);
        tablay=findViewById(R.id.tablay);
        viewPage2=findViewById(R.id.viewpage2);

        viewPage2.setAdapter(new ViewPagerAdepter(this, signInFragment, signUpFragment));


        new TabLayoutMediator(tablay, viewPage2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("Sign in");

                } else
                    tab.setText("Sign up");

            }
        }).attach();
        viewPage2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    tvb1.setText("Welcome");
                    tvs1.setText("Log in to your account to get an update");
                } else {
                    tvb1.setText("Hello!!");
                    tvs1.setText("Register to create a new account");
                }
            }
        });


    }
    public class ViewPagerAdepter extends FragmentStateAdapter {


        public ViewPagerAdepter(@NonNull FragmentActivity fragmentActivity,Sign_inFragment signInFragment , Sign_upFragment signUpFragment ) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {



            if(position==0){
                return new Sign_inFragment();
            }
            else{
                return new Sign_upFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}