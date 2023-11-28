package com.example.imunku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class itemImunisasi extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<DataImunisasi> listImunisasi = new ArrayList<>();
    private Context context;

    public itemImunisasi(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<DataImunisasi> listImunisasi) {
        this.listImunisasi = listImunisasi;
    }

    public void setFilteredList(ArrayList<DataImunisasi> filteredListImun) {
        this.listImunisasi = filteredListImun;
        notifyDataSetChanged();
    }

    public static class imunisasiVH extends RecyclerView.ViewHolder {
        TextView jenisVaksin;
        TextView nama;
        TextView waktuVaksin;
        ConstraintLayout listVaksin;
        public imunisasiVH(@NonNull View itemView) {
            super(itemView);
            this.jenisVaksin = itemView.findViewById(R.id.namaVaksin);
            this.nama = itemView.findViewById(R.id.namaAnak);
            this.waktuVaksin = itemView.findViewById(R.id.waktuVaksin);
            this.listVaksin = itemView.findViewById(R.id.item_Imunisasi);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.activity_item_imunisasi,parent,false);
        return new imunisasiVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        imunisasiVH iVH = (imunisasiVH) holder;
        DataImunisasi curImunisasi = listImunisasi.get(position);
        iVH.jenisVaksin.setText(curImunisasi.getJenisVaksin());
        iVH.nama.setText(curImunisasi.getNamaAnak());
        String tanggalImunisasi = curImunisasi.getHariVaksin() + "/" + curImunisasi.getBulanVaksin() + "/"
            + curImunisasi.getTahunVaksin();
        iVH.waktuVaksin.setText(tanggalImunisasi);

        iVH.listVaksin.setOnClickListener(view -> {
            Intent goEdit = new Intent(this.context, DataVaksinActivity.class);
            goEdit.putExtra("DataImunisasi",curImunisasi);
            this.context.startActivity(goEdit);
        });
    }

    @Override
    public int getItemCount() {
        return listImunisasi.size();
    }
}
