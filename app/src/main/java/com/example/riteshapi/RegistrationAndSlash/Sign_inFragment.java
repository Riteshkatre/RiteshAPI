package com.example.riteshapi.RegistrationAndSlash;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.riteshapi.HomePageActivity;
import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.LoginResponse;
import com.example.riteshapi.R;
import com.example.riteshapi.Variablebags.VeriableBag;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class Sign_inFragment extends Fragment {
    EditText etemail, etpassword;
    String email1,password1;
    Button btnsignin;
    ImageView showpassword;
    private SharedPreference sharedPreference;
    RestCall restCall;
    private boolean isPasswordVisible = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        etemail = view.findViewById(R.id.etemail);
        etpassword = view.findViewById(R.id.etpassword);
        btnsignin = view.findViewById(R.id.btnsignin);
        showpassword=view.findViewById(R.id.showpassword);
        sharedPreference = new SharedPreference(getContext());
        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        if (sharedPreference.isLoggedIn()) {
            openHomePage();

        }else{
            getContext();
        }

        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();


            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email1 = etemail.getText().toString().trim();
                password1 = etpassword.getText().toString().trim();

                if (email1.isEmpty() || password1.isEmpty()) {

                    etemail.setError("Email is required");
                    etemail.requestFocus();
                    etpassword.setError("Password is required");
                    etpassword.requestFocus();

                    Toast.makeText(getActivity(), "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                } else {

                    LoginUser(email1, password1);
                }
            }
        });

        return (view);
    }


    private void openHomePage() {
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    public void LoginUser(String email, String password) {
        restCall.LoginUser("LoginUser", email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("API Error", e.getMessage());
                                Toast.makeText(getActivity(), "This is wrong...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNext(LoginResponse networkResponce) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (networkResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                    etemail.setText("");
                                    etpassword.setText("");
                                    sharedPreference.setLoggedIn(true);
                                    openHomePage();
                                    sharedPreference.setStringvalue("user_id",networkResponce.getUserId());
                                    sharedPreference.setStringvalue("first_name",networkResponce.getFirstName());
                                    sharedPreference.setStringvalue("last_name",networkResponce.getLastName());

                                }
                                Toast.makeText(getActivity(), "" + networkResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showpassword.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_eye_close));
        } else {
            etpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showpassword.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_remove_red_eye_24));
        }

        etpassword.setSelection(etpassword.getText().length());
        isPasswordVisible = !isPasswordVisible;

    }



}