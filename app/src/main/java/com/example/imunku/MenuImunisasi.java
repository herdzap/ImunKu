package com.example.imunku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuImunisasi extends AppCompatActivity implements View.OnClickListener {

    Button back;
    FloatingActionButton buttonTambah;
    SearchView searchView;

    RecyclerView Rv;

    FirebaseAuth ImunAuth;
    FirebaseDatabase DBImun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_imunisasi);

        ImunAuth = FirebaseAuth.getInstance();
        DBImun = FirebaseDatabase.getInstance();

        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        buttonTambah = findViewById(R.id.newImunisasi);
        buttonTambah.setOnClickListener(this);
        searchView = findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Rv = findViewById(R.id.rvVaksin);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        Rv.setLayoutManager(manager);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newImunisasi) {
            Intent intentNewImun = new Intent(this, InsertVaksinActivity.class);
            startActivity(intentNewImun);
        } else if (v.getId() == R.id.backbutton_imunisasi) {
            finish();
        }
    }
}
