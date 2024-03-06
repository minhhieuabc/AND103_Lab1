package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edEmailRegister, edPass, edRePass;
    Button btnRegister, btnBack;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        initListener();
    }

    private void findView(){
        edEmailRegister = findViewById(R.id.edUserNameRegister);
        edPass = findViewById(R.id.edPassWordRegister);
        edRePass = findViewById(R.id.edNhapLai);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ");
    }

    private void initListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
    private void Register(){
        String email = edEmailRegister.getText().toString().trim();
        String password = edPass.getText().toString().trim();
        String laiMk = edRePass.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty() || laiMk.isEmpty()){
            Toast.makeText(this, "Email và mật khẩu không để trống", Toast.LENGTH_SHORT).show();
        } else if (!laiMk.equals(password) ) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
        } else {
            mAuth = FirebaseAuth.getInstance();
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(Register.this, SignIn.class);
                                startActivity(intent);
                                Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}