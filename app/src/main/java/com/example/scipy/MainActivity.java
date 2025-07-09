package com.example.scipy;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;

public class MainActivity extends AppCompatActivity {

    String Email, Pass;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText email, pass;
    Button login;
    TextView reg,forgot;

    SharedPreferences sp;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.ed);
        pass = findViewById(R.id.ed1);
        login = findViewById(R.id.login);
        reg = findViewById(R.id.tv2);
        forgot = findViewById(R.id.tv1);

        sp = getSharedPreferences(ConstantSp.pref, MODE_PRIVATE);

        db = new DBHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

//        // Insert dummy user if not exists (email: test@example.com, pass: 12345678)
//        Cursor checkCursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE email = ?", new String[]{"test@example.com"});
////        if (checkCursor.getCount() == 0) {
////            ContentValues cv = new ContentValues();
////            cv.put("email", "test@example.com");
////            cv.put("pass", "12345678");
////            sqLiteDatabase.insert("user", null, cv);
////        }
////        checkCursor.close();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString().trim();
                Pass = pass.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Enter email");
                } else if (!Email.matches(EmailPattern)) {
                    email.setError("Enter valid email");
                } else if (Pass.isEmpty()) {
                    pass.setError("Enter password");
                } else if (Pass.length() < 8) {
                    pass.setError("Enter 8 digit password");
                } else {
                    Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM user WHERE email=? AND pass=?", new String[]{Email, Pass});
                    if (cursor.getCount() > 0) {
                        while(cursor.moveToNext()) {
                            sp.edit().putString(ConstantSp.userid, cursor.getString(0)).commit();
                            sp.edit().putString(ConstantSp.name, cursor.getString(1)).commit();
                            sp.edit().putString(ConstantSp.password, cursor.getString(2)).commit();
                            sp.edit().putString(ConstantSp.email, cursor.getString(3)).commit();
                            sp.edit().putString(ConstantSp.contact, cursor.getString(4)).commit();
                        }
                        Toast.makeText(MainActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainNavigationActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Forgot_Pass_Activity.class);
                startActivity(intent);
            }
        });
    }
}
