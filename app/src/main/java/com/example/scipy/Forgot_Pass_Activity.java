package com.example.scipy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Forgot_Pass_Activity extends AppCompatActivity {

    String Email, Pass, Passcon;
    EditText email, passnew, passcon;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email = findViewById(R.id.email);
        passnew = findViewById(R.id.passnew);
        passcon = findViewById(R.id.passcon);
        submit = findViewById(R.id.submit);

        DBHelper db = new DBHelper(Forgot_Pass_Activity.this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = email.getText().toString().trim();
                Pass = passnew.getText().toString().trim();
                Passcon = passcon.getText().toString().trim();
                if (Email.isEmpty()) {
                    email.setError("Enter email");
                } else if (Pass.isEmpty()) {
                    passnew.setError("Enter new password");
                } else if (Pass.length() < 8) {
                    passnew.setError("Enter 8 digit password");
                } else if (Passcon.isEmpty()) {
                    passcon.setError("Enter confirm password");
                } else if (!Passcon.equals(Pass)) {
                    passcon.setError("Password not matched");
                } else {
                    sqLiteDatabase.execSQL("UPDATE user SET pass='" + Pass + "' WHERE email='" + Email + "'");
                    email.setText("");
                    passnew.setText("");
                    passcon.setText("");
                    sqLiteDatabase.close();
                    Toast.makeText(Forgot_Pass_Activity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Forgot_Pass_Activity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }
}