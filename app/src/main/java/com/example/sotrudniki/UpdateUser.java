package com.example.sotrudniki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UpdateUser extends AppCompatActivity implements View.OnClickListener  {
    Button saveBtn, deleteBtn;
    EditText etLogin, etPassword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        saveBtn = findViewById(R.id.button);
        saveBtn.setOnClickListener(this);

        deleteBtn = findViewById(R.id.button2);
        deleteBtn.setOnClickListener(this);

        etLogin = findViewById(R.id.Sotrudnik);
        etPassword = findViewById(R.id.sotrudnik);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        Update();

        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLogin.setHint("");
            else
                etLogin.setHint("Логин");
        });
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etPassword.setHint("");
            else
                etPassword.setHint("Пароль");
        });
    }
    public void Update() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_USERS + " WHERE " + DBHelper.KEY_LOGIN + " = '" + MainActivity.user + "'", null);

        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
        int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);

        if (cursor.moveToFirst()) {
            etLogin.setText(cursor.getString(loginIndex));
            etPassword.setText(cursor.getString(passwordIndex));
        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_LOGIN, login);
                contentValues.put(DBHelper.KEY_PASSWORD, password);
                database.update(DBHelper.TABLE_USERS, contentValues,DBHelper.KEY_LOGIN + " = '" + MainActivity.user + "'", null);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.button2:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                int indexStr = outputDB.indexOfChild(outputDBRow);
                int index = 0;
                Cursor cursorUpdater = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_USERS + " WHERE " + DBHelper.KEY_LOGIN + " = '" + MainActivity.user + "'", null);
                if (cursorUpdater != null) {
                    cursorUpdater.moveToPosition(indexStr);
                    index =  cursorUpdater.getInt(0);
                }
                database.delete(DBHelper.TABLE_USERS, DBHelper.KEY_ID1 + " = " + index, null);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                assert cursorUpdater != null;
                cursorUpdater.close();
                break;
        }
    }
}