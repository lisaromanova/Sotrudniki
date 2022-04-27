package com.example.sotrudniki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TripsActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnAdd, btnClear;
    EditText etSotrudnik, etSize, etCity;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etSotrudnik = findViewById(R.id.Sotrudnik);
        etSize = findViewById(R.id.Size);
        etCity = findViewById(R.id.City);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        etSotrudnik.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etSotrudnik.setHint("");
            else
                etSotrudnik.setHint("Сотрудник");
        });

        etSize.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etSize.setHint("");
            else
                etSize.setHint("Размер суточных");
        });

        etCity.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etCity.setHint("");
            else
                etCity.setHint("Город");
        });
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_BUSINESS_TRIPS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_TRIPS);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_KOD_STAFF);
            int sizeIndex = cursor.getColumnIndex(DBHelper.KEY_DAILY);
            int cityIndex = cursor.getColumnIndex(DBHelper.KEY_CITY);

            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputSurname = new TextView(this);
                params.weight = 3.0f;
                outputSurname.setLayoutParams(params);
                outputSurname.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputSurname);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(sizeIndex));
                dbOutputRow.addView(outputName);

                TextView outputDisc = new TextView(this);
                params.weight = 3.0f;
                outputDisc.setLayoutParams(params);
                outputDisc.setText(cursor.getString(cityIndex));
                dbOutputRow.addView(outputDisc);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight=1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdd:
                String name = etCity.getText().toString();
                int size = Integer.parseInt(etSize.getText().toString());
                int sotr = Integer.parseInt(etSotrudnik.getText().toString());

                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_KOD_STAFF, sotr);
                contentValues.put(DBHelper.KEY_DAILY, size);
                contentValues.put(DBHelper.KEY_CITY, name);
                database.insert(DBHelper.TABLE_BUSINESS_TRIPS, null, contentValues);
                UpdateTable();
                etCity.setText("");
                etSize.setText("");
                etSotrudnik.setText("");

                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_BUSINESS_TRIPS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_BUSINESS_TRIPS, DBHelper.KEY_ID_POSITIONS+" = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_BUSINESS_TRIPS, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID_TRIPS);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_KOD_STAFF);
                    int discIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CITY);
                    int surnameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_DAILY);

                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID_TRIPS, realID);
                            contentValues.put(DBHelper.KEY_KOD_STAFF, cursorUpdater.getString(surnameIndex));
                            contentValues.put(DBHelper.KEY_CITY, cursorUpdater.getString(discIndex));
                            contentValues.put(DBHelper.KEY_DAILY, cursorUpdater.getString(nameIndex));


                            database.replace(DBHelper.TABLE_BUSINESS_TRIPS, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        database.delete(DBHelper.TABLE_BUSINESS_TRIPS, DBHelper.KEY_ID_TRIPS + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();

                }
                cursorUpdater.close();
                break;

        }
    }
}