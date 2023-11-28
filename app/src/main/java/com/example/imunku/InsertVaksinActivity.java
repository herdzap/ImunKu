package com.example.imunku;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

public class InsertVaksinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    TextView judulHalaman;
    EditText namaAnak, umurAnak, tanggalLahir, tanggalVaksin;
    Button simpanVaksin, batalVaksin;
    ImageButton gambarVaksin;
    ImageView previewVaksin;
    String jenisVaksin = "kosong";
    int vaksinKe = 0;
    int hariLahir, bulanLahir, tahunLahir;
    int hariVaksin, bulanVaksin, tahunVaksin;
    DataImunisasi editData;
    boolean isEdit = false;
    final DataImunisasi[] cloneImunisasi = new DataImunisasi[1];

    String currImunUID;

    // firebase component
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String currGambarId;
    private StorageReference refUploaded;
    private int year, month, day;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahdata);

        // form and button init
        namaAnak = findViewById(R.id.namaAnak);
        umurAnak = findViewById(R.id.umurAnak);

        tanggalLahir = findViewById(R.id.tanggalLahir);
        tanggalVaksin = findViewById(R.id.tanggalVaksin);
        simpanVaksin = findViewById(R.id.simpanVaksin);
        batalVaksin = findViewById(R.id.batalVaksin);
        gambarVaksin = findViewById(R.id.gambarVaksin);
        previewVaksin = findViewById(R.id.previewVaksin);

        // jenisVaksin spinner init
        Spinner spinnerVaksin = (Spinner) findViewById(R.id.jenisVaksin);
        ArrayAdapter<CharSequence> adapterJenis = ArrayAdapter.createFromResource(this,
                R.array.jenis_vaksin, android.R.layout.simple_spinner_item);
        adapterJenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaksin.setAdapter(adapterJenis);
        spinnerVaksin.setOnItemSelectedListener(this);

        // vaksinKe spinner init
        Spinner spinnerJumlahVaksin = (Spinner) findViewById(R.id.vaksinKe);
        ArrayAdapter<CharSequence> adapterJumlah = ArrayAdapter.createFromResource(this,
                R.array.vaksin_ke, android.R.layout.simple_spinner_item);
        adapterJumlah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJumlahVaksin.setAdapter(adapterJumlah);
        spinnerJumlahVaksin.setOnItemSelectedListener(this);

        // firebase component init
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("imunisasi");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference(Objects.requireNonNull(mAuth.getUid()));

        simpanVaksin.setOnClickListener(this);
        batalVaksin.setOnClickListener(this);
        tanggalLahir.setOnClickListener(this);
        tanggalVaksin.setOnClickListener(this);
        gambarVaksin.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // check if user currently adding new/update existing imunisasi
        editData = getIntent().getParcelableExtra("DataImunisasi");
        if (!Objects.isNull(editData)) {
            judulHalaman = findViewById(R.id.judulHalamanForm);
            judulHalaman.setText("Edit Data Imunisasi");
            simpanVaksin.setText("Simpan Perubahan");
            isEdit = true;

            //default value
            namaAnak.setText(editData.getNamaAnak(), TextView.BufferType.EDITABLE);
            umurAnak.setText(String.valueOf(editData.getUmur()), TextView.BufferType.EDITABLE);

            hariLahir = editData.getHariLahir();
            bulanLahir = editData.getBulanLahir();
            tahunLahir = editData.getTahunLahir();
            String textTanggalLahir = hariLahir+"/"+bulanLahir+"/"+tahunLahir;
            tanggalLahir.setText(textTanggalLahir, TextView.BufferType.EDITABLE);

            hariVaksin = editData.getHariVaksin();
            bulanVaksin = editData.getBulanVaksin();
            tahunVaksin = editData.getTahunVaksin();
            String textTanggalVaksin = hariVaksin+"/"+bulanVaksin+"/"+tahunVaksin;
            tanggalVaksin.setText(textTanggalVaksin, TextView.BufferType.EDITABLE);

        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.simpanVaksin && !isEdit) {
            uploadImage();
            uploadData(namaAnak.getText().toString(), Integer.parseInt(umurAnak.getText().toString())
                    , hariLahir, bulanLahir, tahunLahir, hariVaksin, bulanVaksin, tahunVaksin
                    , jenisVaksin, vaksinKe, currGambarId);
            finish();
        } else if (v.getId() == R.id.simpanVaksin && isEdit) {
            hapusGambar(editData.getIdGambar());
            uploadImage();
            updateData(this.editData, namaAnak.getText().toString(), Integer.parseInt(umurAnak.getText().toString())
                    ,hariLahir,bulanLahir,tahunLahir,hariVaksin,bulanVaksin, tahunVaksin,jenisVaksin
                    ,vaksinKe,currGambarId);
            finish();
        } else if (v.getId() == R.id.batalVaksin) {
            finish();
        } else if (v.getId() == R.id.tanggalLahir) {
            showDatePicker(R.id.tanggalLahir);
        } else if (v.getId() == R.id.tanggalVaksin) {
            showDatePicker(R.id.tanggalVaksin);
        } else if (v.getId() == R.id.gambarVaksin) {
            selectImage();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        if (id == R.id.jenisVaksin) {
            jenisVaksin = adapterView.getItemAtPosition(i).toString();
            Log.d(TAG, "onItemSelected: " + jenisVaksin);
        } else if (id == R.id.vaksinKe) {
            vaksinKe = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                22);
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Defining the child of storageReference
            this.currGambarId = UUID.randomUUID().toString();
            refUploaded = storageReference
                    .child(this.currGambarId);

            // adding listeners on upload
            // or failure of image
            refUploaded.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                // Image uploaded successfully
                                // Dismiss dialog
                                Toast
                                        .makeText(InsertVaksinActivity.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(e -> {
                        // Error, Image not uploaded
                        Toast
                                .makeText(InsertVaksinActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    });
        }
    }

    private void hapusGambar(String idGambar) {
        if (filePath!=null) {
            storageReference.child(idGambar).delete().addOnSuccessListener(unused -> {
                Log.d(TAG, "hapusGambar: hapus berhasil");
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                previewVaksin.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void showDatePicker(int viewId) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertVaksinActivity.this,
                (datePicker, year, month, dayOfMonth) -> {
                    month += 1;
                    String date = dayOfMonth+"/"+month+"/"+year;
                    if (viewId == R.id.tanggalLahir) {
                        hariLahir = dayOfMonth;
                        bulanLahir = month;
                        tahunLahir = year;
                        tanggalLahir.setText(date);
                    } else if (viewId == R.id.tanggalVaksin) {
                        hariVaksin = dayOfMonth;
                        bulanVaksin = month;
                        tahunVaksin = year;
                        tanggalVaksin.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void uploadData(String namaAnak, int umur, int hariLahir, int bulanLahir, int tahunLahir,
                            int hariVaksin, int bulanVaksin, int tahunVaksin, String jenisVaksin,
                            int vaksinKe, String idGambar) {
        DatabaseReference newImun = databaseReference.child(Objects.requireNonNull(mAuth.getUid()))
                .push();
        Log.d(TAG, "uploadData: "+idGambar);
        DataImunisasi imunisasi = new DataImunisasi(namaAnak, umur, hariLahir, bulanLahir, tahunLahir,
                hariVaksin, bulanVaksin, tahunVaksin, jenisVaksin, vaksinKe, idGambar);
        currImunUID = newImun.getKey();
        imunisasi.setUuid(newImun.getKey());
        newImun.setValue(imunisasi)
                .addOnSuccessListener(this,
                        unused -> Toast.makeText(InsertVaksinActivity.this, "Tambah " +
                                "data berhasil!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(this,
                        e -> Toast.makeText(InsertVaksinActivity.this,
                                "Gagal menambah data", Toast.LENGTH_SHORT).show());
    }


    private void updateData(DataImunisasi editImunisasi, String namaAnak, int umur, int hariLahir,
                            int bulanLahir, int tahunLahir, int hariVaksin, int bulanVaksin,
                            int tahunVaksin, String jenisVaksin, int vaksinKe, String idGambar) {

        Intent intent = new Intent();

        if (filePath == null) {
            idGambar = editImunisasi.getIdGambar();
        }
        String finalIdGambar = idGambar;
        DataImunisasi imunisasi = new DataImunisasi(namaAnak, umur, hariLahir, bulanLahir,
                tahunLahir, hariVaksin, bulanVaksin, tahunVaksin, jenisVaksin, vaksinKe, finalIdGambar);
        imunisasi.setUuid(editImunisasi.getUuid());
        cloneImunisasi[0] = new DataImunisasi(namaAnak, umur, hariLahir, bulanLahir,
                tahunLahir, hariVaksin, bulanVaksin, tahunVaksin, jenisVaksin, vaksinKe, finalIdGambar);
        cloneImunisasi[0].setUuid(editImunisasi.getUuid());
        query(editImunisasi.getUuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(MotionEffect.TAG, "onDataChange: masuk, "+snapshot.exists());

                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                DataSnapshot dataQuery = iterator.next();
                Log.d(MotionEffect.TAG, "onDataChange: " + dataQuery.toString());
                dataQuery.getRef().setValue(imunisasi);

                Toast.makeText(InsertVaksinActivity.this, "Ubah " +
                        "data berhasil!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(MotionEffect.TAG, "onCancelled: ", error.toException());
            }

        });
        onBackPressed();
    }

    private Query query(String uuid) {
        return databaseReference
                .child(Objects.requireNonNull(mAuth.getUid()))
                .orderByChild("uuid")
                .equalTo(uuid);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Log.d(TAG, "onBackPressed: "+cloneImunisasi[0].getIdGambar());
        intent.putExtra("DataImunisasi", cloneImunisasi[0]);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            getParent().setResult(Activity.RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}
