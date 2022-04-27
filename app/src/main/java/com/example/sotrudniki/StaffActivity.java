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

public class StaffActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnClear;
    EditText etSurname, etFirstName, etLastName, etPosition;

    DBHelper dbHelper;
    SQLiteDatabase database, db;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etSurname = findViewById(R.id.Sotrudnik);
        etFirstName = findViewById(R.id.Size);
        etLastName = findViewById(R.id.City);
        etPosition = findViewById(R.id.Position);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();
        etSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etSurname.setHint("");
            else
                etSurname.setHint("Фамилия");
        });

        etFirstName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etFirstName.setHint("");
            else
                etFirstName.setHint("Имя");
        });

        etLastName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLastName.setHint("");
            else
                etLastName.setHint("Отчество");
        });
        etPosition.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etPosition.setHint("");
            else
                etPosition.setHint("Должность");
        });
        UpdateTable();
    }

    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_STAFF, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_STAFF);
            int surnameIndex = cursor.getColumnIndex(DBHelper.KEY_SURNAME);
            int firstnameIndex = cursor.getColumnIndex(DBHelper.KEY_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(DBHelper.KEY_LAST_NAME);
            int postIndex = cursor.getColumnIndex(DBHelper.KEY_KOD_POST);


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
                outputSurname.setText(cursor.getString(surnameIndex));
                dbOutputRow.addView(outputSurname);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(firstnameIndex));
                dbOutputRow.addView(outputName);

                TextView outputDisc = new TextView(this);
                params.weight = 3.0f;
                outputDisc.setLayoutParams(params);
                outputDisc.setText(cursor.getString(lastNameIndex));
                dbOutputRow.addView(outputDisc);

                TextView outputPost = new TextView(this);
                params.weight = 3.0f;
                outputDisc.setLayoutParams(params);
                outputDisc.setText(cursor.getString(postIndex));
                dbOutputRow.addView(outputPost);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
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
                String surname = etSurname.getText().toString();
                String name = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                int position = Integer.parseInt(etPosition.getText().toString());
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_SURNAME, surname);
                contentValues.put(DBHelper.KEY_FIRST_NAME, name);
                contentValues.put(DBHelper.KEY_LAST_NAME, lastname);
                contentValues.put(DBHelper.KEY_KOD_POST, position);
                try {
                    database.insert(DBHelper.TABLE_STAFF, null, contentValues);
                    UpdateTable();
                    etSurname.setText("");
                    etFirstName.setText("");
                    etLastName.setText("");
                    etPosition.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    break;
                }


            case R.id.btnClear:
                database.delete(DBHelper.TABLE_STAFF, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_STAFF, DBHelper.KEY_ID_STAFF+" = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_STAFF, null, null, null, null, null, null);
                if(cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID_STAFF);
                    int surnameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_SURNAME);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_FIRST_NAME);
                    int lastnameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_LAST_NAME);
                    int discIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_KOD_POST);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID_STAFF, realID);
                            contentValues.put(DBHelper.KEY_SURNAME, cursorUpdater.getString(surnameIndex));
                            contentValues.put(DBHelper.KEY_FIRST_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_LAST_NAME, cursorUpdater.getString(lastnameIndex));
                            contentValues.put(DBHelper.KEY_KOD_POST, cursorUpdater.getString(discIndex));

                            database.replace(DBHelper.TABLE_STAFF, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast()) {
                        database.delete(DBHelper.TABLE_STAFF, DBHelper.KEY_ID_STAFF + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();

                }
                cursorUpdater.close();
                break;

        }
    }
}