package com.example.imunku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class BerandaActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout menuImunisasi, menuTentangkami, menuArtikel;

    TextView textKeluar;

    FirebaseAuth ImunAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        //deklarasi widget & listener
        menuImunisasi = findViewById(R.id.card_imunisasi);
        menuImunisasi.setOnClickListener(this);
        menuTentangkami = findViewById(R.id.card_about);
        menuTentangkami.setOnClickListener(this);
        menuArtikel = findViewById(R.id.card_artikel);
        menuArtikel.setOnClickListener(this);
        textKeluar = findViewById(R.id.textLogout);
        textKeluar.setOnClickListener(this);

        ImunAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        int idView = view.getId();

        if (idView == R.id.card_imunisasi){
            Intent MenuImunisasi = new Intent(this, MenuImunisasi.class);
            startActivity(MenuImunisasi);
        } else if (idView == R.id.card_about) {
            Intent MenuTentangKami = new Intent(this, tentangkami.class);
            startActivity(MenuTentangKami);
        } else if (idView == R.id.card_artikel) {
            Intent MenuArtikel = new Intent(this, ArtikelActivity.class);
            startActivity(MenuArtikel);
        } else if (idView == R.id.textLogout) {
            Toast.makeText(this, "LogOut Berhasil",Toast.LENGTH_SHORT).show();
            ImunAuth.signOut();
            Intent keluar = new Intent(this, LoginActivity.class );
            startActivity(keluar);
        }
    }
}
