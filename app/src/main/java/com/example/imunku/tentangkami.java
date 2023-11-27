package com.example.imunku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tentangkami extends AppCompatActivity implements View.OnClickListener {

    Button back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentangkami);
        back = findViewById(R.id.backbutton_imunisasi);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent backIntent = new Intent(this, BerandaActivity.class);
        startActivity(backIntent);
    }
}
