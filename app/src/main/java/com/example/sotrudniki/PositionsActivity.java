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

public class PositionsActivity extends AppCompatActivity implements View.OnClickListener  {

    Button btnAdd, btnClear;
    EditText etName, etDisc, etSalary;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.Sotrudnik);
        etSalary = findViewById(R.id.City);
        etDisc = findViewById(R.id.Size);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etName.setHint("");
            else
                etName.setHint("Название");
        });

        etSalary.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etSalary.setHint("");
            else
                etSalary.setHint("Оклад");
        });

        etDisc.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etDisc.setHint("");
            else
                etDisc.setHint("Разряд");
        });
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_POSITIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_POSITIONS);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int salaryIndex = cursor.getColumnIndex(DBHelper.KEY_DISCHARGE);
            int discIndex = cursor.getColumnIndex(DBHelper.KEY_SALARY);

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
                outputName.setText(cursor.getString(discIndex));
                dbOutputRow.addView(outputName);

                TextView outputDisc = new TextView(this);
                params.weight = 3.0f;
                outputDisc.setLayoutParams(params);
                outputDisc.setText(cursor.getString(salaryIndex));
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
                String name = etName.getText().toString();
                int disc = Integer.parseInt(etDisc.getText().toString());
                int salary = Integer.parseInt(etSalary.getText().toString());

                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_TITLE, name);
                contentValues.put(DBHelper.KEY_DISCHARGE, disc);
                contentValues.put(DBHelper.KEY_SALARY, salary);
                database.insert(DBHelper.TABLE_POSITIONS, null, contentValues);
                UpdateTable();
                etName.setText("");
                etSalary.setText("");
                etDisc.setText("");

                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_POSITIONS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_POSITIONS, DBHelper.KEY_ID_POSITIONS+" = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_POSITIONS, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID_POSITIONS);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_TITLE);
                    int discIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_SALARY);
                    int surnameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_DISCHARGE);

                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID_POSITIONS, realID);
                            contentValues.put(DBHelper.KEY_TITLE, cursorUpdater.getString(surnameIndex));
                            contentValues.put(DBHelper.KEY_SALARY, cursorUpdater.getString(discIndex));
                            contentValues.put(DBHelper.KEY_DISCHARGE, cursorUpdater.getString(nameIndex));


                            database.replace(DBHelper.TABLE_POSITIONS, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        database.delete(DBHelper.TABLE_POSITIONS, DBHelper.KEY_ID_POSITIONS + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();

                }
                cursorUpdater.close();
                break;

        }
    }
}