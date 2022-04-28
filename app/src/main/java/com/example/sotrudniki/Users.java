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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Users extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnClear;
    EditText etLogin, etPassword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    String adminUser = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);


        etLogin = findViewById(R.id.Size);
        etPassword = findViewById(R.id.City);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();

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
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID1);
            int loginRouteIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
            int passwordRouteIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);

            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                if(!adminUser.equals(cursor.getString(loginRouteIndex))) {
                    TableRow dbOutputRow = new TableRow(this);
                    dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                    TextView outputID = new TextView(this);
                    params.weight = 3.0f;
                    outputID.setLayoutParams(params);
                    outputID.setText(cursor.getString(idIndex));
                    outputID.setTextSize(12);
                    dbOutputRow.addView(outputID);

                    TextView outputLogin = new TextView(this);
                    params.weight = 3.0f;
                    outputLogin.setLayoutParams(params);
                    outputLogin.setText(cursor.getString(loginRouteIndex));
                    outputLogin.setTextSize(12);
                    dbOutputRow.addView(outputLogin);

                    TextView outputPassword = new TextView(this);
                    params.weight = 3.0f;
                    outputPassword.setLayoutParams(params);
                    outputPassword.setText(cursor.getString(passwordRouteIndex));
                    outputPassword.setTextSize(12);
                    dbOutputRow.addView(outputPassword);

                    Button updateBtn = new Button(this);
                    updateBtn.setOnClickListener(this);
                    params.weight = 1.0f;
                    updateBtn.setLayoutParams(params);
                    updateBtn.setText("Изменить\nзапись");
                    updateBtn.setTextSize(12);
                    updateBtn.setId(cursor.getInt(idIndex));
                    dbOutputRow.addView(updateBtn);

                    dbOutput.addView(dbOutputRow);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_LOGIN, login);
                contentValues.put(DBHelper.KEY_PASSWORD, password);

                database.insert(DBHelper.TABLE_USERS, null, contentValues);
                etLogin.setText("");
                etPassword.setText("");
                UpdateTable();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_USERS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:

                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                int indexStr = outputDB.indexOfChild(outputDBRow);
                int index = 0;
                Cursor cursorUpdater = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_USERS + " WHERE " + DBHelper.KEY_LOGIN + " <> '" + adminUser + "'", null);

                if (cursorUpdater != null) {
                    cursorUpdater.moveToPosition(indexStr);
                    index =  cursorUpdater.getInt(0);
                }
                Intent intent = new Intent(this, UpdateUserAdmin.class);
                Bundle b = new Bundle();
                b.putInt("key", index);
                intent.putExtras(b);
                startActivity(intent);
                finish();
                assert cursorUpdater != null;
                cursorUpdater.close();
                break;
        }
    }
}