package com.example.imunku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrasiActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth ImunAuth;
    private EditText email, password;
    private Button daftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        daftar = findViewById(R.id.daftar);
        ImunAuth = FirebaseAuth.getInstance();
        daftar.setOnClickListener(this);
    }

    public void onClick(View view){
        if (view.getId() == R.id.daftar){
            //panggil method daftar
            daftar();
        } else {
            throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void nextActivity(boolean isSuccess, String message) {
        Toast.makeText(RegistrasiActivity.this,message, Toast.LENGTH_SHORT).show();
        if (isSuccess) {
            Intent intent = new Intent(this, BerandaActivity.class);
            startActivity(intent);
        }
    }

    private void daftar(){
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        ImunAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnSuccessListener(authResult -> {
                    nextActivity(true, "Daftar akun berhasil!");
                })
                .addOnFailureListener(authResult -> {
                    nextActivity(false, "Daftar akun gagal, coba lagi!");
                });
    }
}
