package com.example.riteshapi.RegistrationAndSlash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riteshapi.R;
import com.example.riteshapi.Variablebags.VeriableBag;

import com.example.riteshapi.Network.RestCall;
import com.example.riteshapi.Network.RestClient;
import com.example.riteshapi.NetworkResponce.CommonUserResponce;
import rx.Subscriber;
import rx.schedulers.Schedulers;


public class Sign_upFragment extends Fragment {
    EditText etfirstname, etlastname, etemail, etpassword;
    Button btnsubmit;
    RestCall restCall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        etfirstname = view.findViewById(R.id.etfirstname);
        etlastname = view.findViewById(R.id.etlastname);
        etemail = view.findViewById(R.id.etemail);
        etpassword = view.findViewById(R.id.etpassword);
        btnsubmit = view.findViewById(R.id.btnsubmit);
        restCall = RestClient.createService(RestCall.class, VeriableBag.BASE_URL, VeriableBag.API_KEY);
        btnsubmit.setOnClickListener(v -> {
            String firstName = etfirstname.getText().toString().trim();
            String lastName = etlastname.getText().toString().trim();
            String email = etemail.getText().toString().trim();
            String password = etpassword.getText().toString().trim();
            if (TextUtils.isEmpty(firstName)) {
                etfirstname.setError("First name is required");
                etfirstname.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(lastName)) {
                etlastname.setError("Last name is required");
                etlastname.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etemail.setError("Email is required");
                etemail.requestFocus();
                return;
            }
            if (!isValidEmail(email)) {
                etemail.setError("Invalid email format (format should be abc@gmail.com)");
                etemail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etpassword.setError("Password is required");
                etpassword.requestFocus();
                return;
            } else if (!isValidPassword(password)) {
                etpassword.setError("password must be 8 character and alphabetic and numeric there");
            } else {
                AddUser(firstName, lastName, email, password);
//                    Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_SHORT).show();

            }
        });
        return (view);
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            etpassword.setError("Password is required");
            etpassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            etpassword.setError("Password should be at least 8 characters");
            etpassword.requestFocus();
            return false;
        }
        boolean hasAlphabetic = false;
        boolean hasNumeric = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasAlphabetic = true;
            } else if (Character.isDigit(c)) {
                hasNumeric = true;
            }
            if (hasAlphabetic && hasNumeric) {
                break;
            }
        }

        if (!hasAlphabetic || !hasNumeric) {
            etpassword.setError("Password must contain both alphabetic and numeric characters");
            etpassword.requestFocus();
            return false;
        }
        if (!containsSpecialCharacter(password)) {
            etpassword.setError("Password must contain at least one special character");
            etpassword.requestFocus();
            return false;
        }
        return true;
    }
    private boolean containsSpecialCharacter(String str) {
        String specialCharacters = "!@#$%^&*()-_+=<>?/[]{}|";
        for (char c : str.toCharArray()) {
            if (specialCharacters.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
    public void AddUser(String first_name, String last_name, String email, String password) {
        restCall.AddUser("AddUser", first_name, last_name, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<CommonUserResponce>() {
                    @Override
                    public void onCompleted() {
                        /*saveUserDataLocally(first_name, last_name);*/

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
                    public void onNext(CommonUserResponce commonUserResponce) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (commonUserResponce.getStatus().equalsIgnoreCase(VeriableBag.SUCCESS_CODE)) {
                                        etfirstname.setText("");
                                        etlastname.setText("");
                                        etemail.setText("");
                                        etpassword.setText("");
                                        getActivity();
                                    }
                                    Toast.makeText(getActivity(), "" + commonUserResponce.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                });


    }

}