package com.example.sotrudniki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStaff, btnTrips, btnPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnStaff = findViewById(R.id.btnStaff);
        btnTrips = findViewById(R.id.btnTrips);
        btnPositions = findViewById(R.id.btnPositions);
        btnStaff.setOnClickListener(this);
        btnTrips.setOnClickListener(this);
        btnPositions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnStaff:
                startActivity(new Intent(this, StaffActivity.class));
                break;
            case R.id.btnTrips:
                startActivity(new Intent(this, TripsActivity.class));
                break;
            case R.id.btnPositions:
                startActivity(new Intent(this, PositionsActivity.class));
                break;

        }
    }
}