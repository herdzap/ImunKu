package com.example.imunku;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth ImunAuth;
    private EditText email, password;
    private Button masuk;
    private TextView daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declare listener
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        masuk = findViewById(R.id.masuk);
        daftar = findViewById(R.id.daftar);
        ImunAuth = FirebaseAuth.getInstance();
        masuk.setOnClickListener(this);
        daftar.setOnClickListener(this);
    }

    //Buat method cek login

    //Switch case login dan register
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.masuk) {
            login();

        } else if (viewId == R.id.daftar) {
            //method register;
            registrasi();
        } else {

        }
    }

    //Buat Method register
    private void registrasi(){
        Intent registerIntent = new Intent(this, RegistrasiActivity.class);
        startActivity(registerIntent);
    }
    //Method buat login
    private void login() {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        ImunAuth.signInWithEmailAndPassword(emailInput,passwordInput)
            .addOnSuccessListener(authResult -> {
                Log.d(TAG, "login: Successfull");
                nextActivity(true, "Login berhasil!");
            })
            .addOnFailureListener(authResult -> {
                Log.d(TAG, "login: failed");
                nextActivity(false, "Login gagal, pastikan email dan password benar!");
            });
    }

    //Method next activity
    private void nextActivity(boolean isSuccess, String message) {
        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
        if (isSuccess) {
            Intent intent = new Intent(this, BerandaActivity.class);
            startActivity(intent);
        }
    }
}
