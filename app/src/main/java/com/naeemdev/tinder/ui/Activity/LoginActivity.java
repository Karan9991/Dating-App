package com.naeemdev.tinder.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naeemdev.tinder.R;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private FirebaseAuth userAuth;
    private EditText loginEmail;
    private EditText loginPass;
    private Button btnLoginPageLogin;
    private Button btnLoginPageRegister;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {

        setContentView(R.layout.activity_login);


        loginEmail = findViewById(R.id.loginEmailText);
        loginPass = findViewById(R.id.loginPassText);
        btnLoginPageLogin = findViewById(R.id.btnLoginPageLogin);
        btnLoginPageRegister = findViewById(R.id.btnLoginPageRegister);


        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        RelativeLayout forgotPasswordLayout = findViewById(R.id.forgotPasswordLayout);
        forgotPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up bottom sheet
                showForgotPasswordBottomSheet();
            }
        });
//            btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    String userEmail = loginEmail.getText().toString();
//                    String userPass = loginPass.getText().toString();
//
//                    if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty((userPass))) {
//
//                        dialog.show();
//
//                        userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    sendToMain();
//                                    dialog.dismiss();
//                                } else {
//                                    String errorMessage = task.getException().getMessage();
//                                    Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
//                                    dialog.dismiss();
//                                }
//                            }
//                        });
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        //original
        btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = loginEmail.getText().toString();
                String userPass = loginPass.getText().toString();

                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)) {

                    dialog.show();

                    userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = userAuth.getCurrentUser();
                                if (user != null && user.isEmailVerified()) {
                                    sendToMain();
                                    dialog.dismiss();
                                } else {
                                    emailVerificationDialog();
                                    Toast.makeText(LoginActivity.this, "Please verify your email address before logging in.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your email and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLoginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    }


    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showForgotPasswordBottomSheet() {
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.forgot_password_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText emailEditText = bottomSheetView.findViewById(R.id.emailEditText);
        Button resetPasswordButton = bottomSheetView.findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                // Perform reset password logic here using the entered email
                if (!TextUtils.isEmpty(email)) {
                    // Call the reset password function
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Reset password email sent.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                    dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void emailVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Email Not Verified")
                .setMessage("Email verification required. Please check your email to verify your account.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
