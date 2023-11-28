package com.example.imunku;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DataVaksinActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_EDIT_IMUNISASI = 201;
    DataImunisasi currImunisasi;
    TextView namaAnakTV, umurAnakTV, tanggalLahirTV, jenisVaksinTV, tanggalVaksinTV, vaksinKeTV;
    ImageView previewImunisasiIV;
    Button editBtn, hapusBtn;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;
    StorageReference refGambarVaksin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datavaksin);

        // form var initialization
        namaAnakTV = findViewById(R.id.readNamaAnak);
        umurAnakTV = findViewById(R.id.readUmurAnak);
        tanggalLahirTV = findViewById(R.id.valueTanggalLahir);
        jenisVaksinTV = findViewById(R.id.valueJenisVaksin);
        tanggalVaksinTV = findViewById(R.id.valueTanggalVaksin);
        vaksinKeTV = findViewById(R.id.valVaksinKe);
        previewImunisasiIV = findViewById(R.id.previewVaksin);

        // form btn initialization
        editBtn = findViewById(R.id.editVaksinBtn);
        hapusBtn = findViewById(R.id.hapusVaksinBtn);
        editBtn.setOnClickListener(this);
        hapusBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("imunisasi");

        currImunisasi = getIntent().getParcelableExtra("DataImunisasi");

        setData();
        setImage();
    }

    private void setData() {
        namaAnakTV.setText(currImunisasi.getNamaAnak());
        umurAnakTV.setText(currImunisasi.getUmur() + " tahun");
        tanggalLahirTV.setText(currImunisasi.getHariLahir() + "/" + currImunisasi.getBulanLahir()
                + "/" + currImunisasi.getTahunLahir());
        jenisVaksinTV.setText(currImunisasi.getJenisVaksin());
        tanggalVaksinTV.setText(currImunisasi.getHariVaksin() + "/" + currImunisasi.getBulanVaksin()
                + "/" + currImunisasi.getTahunVaksin());
        Log.d(TAG, "onCreate: "+currImunisasi.getBulanVaksin());
        vaksinKeTV.setText(String.valueOf(currImunisasi.getVaksinKe()));
    }

    private void setImage() {
        storageReference = FirebaseStorage.getInstance()
                .getReference(Objects.requireNonNull(mAuth.getUid()));
        if (currImunisasi.getIdGambar()!=null) {
            refGambarVaksin = storageReference
                    .child(currImunisasi.getIdGambar());
            refGambarVaksin.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Log.d(TAG, "onCreate: " + uri);
                        com.bumptech.glide.Glide.with(this)
                                .load(uri)
                                .into(previewImunisasiIV);
                    });
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editVaksinBtn) {
            Intent startEdit = new Intent(this, InsertVaksinActivity.class);
            startEdit.putExtra("DataImunisasi",currImunisasi);
            activityResultLauncher.launch(startEdit);
        } else if (view.getId() == R.id.hapusVaksinBtn) {
            hapusData();
        }
    }

    private void hapusData() {
        hapusGambar();
        query(currImunisasi.getUuid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataQuery : snapshot.getChildren()) {
                            dataQuery.getRef().removeValue();
                            Log.d(TAG, "onDataChange: delete success");
                        }
                        Toast.makeText(DataVaksinActivity.this, "Hapus " +
                                "data berhasil!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DataVaksinActivity.this, "Error, Hapus " +
                                "data gagal!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hapusGambar() {
        refGambarVaksin.delete().addOnSuccessListener(unused -> {
            Log.d(TAG, "hapusGambar: hapus berhasil");
        });
    }

    private Query query(String uuid) {
        return databaseReference
                .child(Objects.requireNonNull(mAuth.getUid()))
                .orderByChild("uuid")
                .equalTo(uuid);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i(TAG, "onActivityResult: actresult dipanggil"+result.getResultCode());
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            currImunisasi = data.getParcelableExtra("DataImunisasi");
                            Log.d(TAG, "readVaksin onActivityResult: "+currImunisasi.getNamaAnak());
                            setData();
                            setImage();
                        } else {
                            Log.i(TAG, "onActivityResult: data kosong");
                            Log.i(TAG, "onActivityResult: data kosong");
                        }
                    }
                }
            }
    );
}
