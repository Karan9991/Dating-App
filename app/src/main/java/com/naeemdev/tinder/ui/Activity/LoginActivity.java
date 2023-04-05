package com.naeemdev.tinder.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.naeemdev.tinder.R;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private FirebaseAuth userAuth;
    private EditText loginEmail;
    private EditText loginPass;
    private Button btnLoginPageLogin;
    private Button btnLoginPageRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmailText);
        loginPass = findViewById(R.id.loginPassText);
        btnLoginPageLogin = findViewById(R.id.btnLoginPageLogin);
        btnLoginPageRegister = findViewById(R.id.btnLoginPageRegister);


        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);

        btnLoginPageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = loginEmail.getText().toString();
                String userPass = loginPass.getText().toString();

                if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty((userPass))) {

                    dialog.show();

                    userAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendToMain();
                                dialog.dismiss();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
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


    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
