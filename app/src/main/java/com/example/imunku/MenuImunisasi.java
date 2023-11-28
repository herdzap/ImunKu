package com.example.imunku;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuImunisasi extends AppCompatActivity implements View.OnClickListener {

    Button back;
    FloatingActionButton buttonTambah;
    SearchView searchView;

    RecyclerView Rv;

    itemImunisasi RvAdapter;

    FirebaseAuth ImunAuth;
    FirebaseDatabase DBImun;
    DatabaseReference DBReference;

    ArrayList<DataImunisasi> listImunisasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_imunisasi);

        back = findViewById(R.id.backbutton);
        back.setOnClickListener(this);
        buttonTambah = findViewById(R.id.newImunisasi);
        buttonTambah.setOnClickListener(this);
        searchView = findViewById(R.id.searchBar);
        searchView.clearFocus();

        ImunAuth = FirebaseAuth.getInstance();
        DBImun = FirebaseDatabase.getInstance();
        DBReference = DBImun.getReference("imunisasi");

        Rv = findViewById(R.id.rvVaksin);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        Rv.setLayoutManager(manager);
        RvAdapter = new itemImunisasi(this);
        Rv.setAdapter(RvAdapter);

        loadData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

    }

    private void filterList(String s) {
        ArrayList<DataImunisasi> filteredListImun = new ArrayList<>();
        for (DataImunisasi imunisasi : listImunisasi) {
            if (imunisasi.getNamaAnak().toLowerCase().contains(s.toLowerCase())) {
                filteredListImun.add(imunisasi);
            }
        }
        if (filteredListImun.isEmpty()) {
            Toast.makeText(this, "imunisasi tidak ditemukan",Toast.LENGTH_SHORT).show();
        } else {
            RvAdapter.setFilteredList(filteredListImun);
        }
    }

    private Query get() {
        return DBReference
            .child(ImunAuth.getUid())
            .orderByKey();
    }

    private void loadData() {
        get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listImunisasi = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DataImunisasi dataImunisasi = data.getValue(DataImunisasi.class);
                    listImunisasi.add(dataImunisasi);
                }
                RvAdapter.setItems(listImunisasi);
                RvAdapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: Load Dipanggil");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newImunisasi) {
            Intent intentNewImun = new Intent(this, InsertVaksinActivity.class);
            startActivity(intentNewImun);
        } else if (v.getId() == R.id.backbutton) {
            finish();
        }
    }
}
