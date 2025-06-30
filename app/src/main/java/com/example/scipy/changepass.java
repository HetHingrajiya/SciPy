package com.example.scipy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class changepass extends AppCompatActivity {

    EditText nameold, passnew, passcon;
    Button chg;

    String Nameold, Passnew, Passcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changepass);

        nameold = findViewById(R.id.nameold);
        passnew = findViewById(R.id.passnew);
        passcon = findViewById(R.id.passcon);
        chg = findViewById(R.id.chg);

        DBHelper db = new DBHelper(changepass.this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        chg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nameold = nameold.getText().toString().trim();
                Passnew = passnew.getText().toString().trim();
                Passcon = passcon.getText().toString().trim();
                if (Nameold.isEmpty()) {
                    nameold.setError("Enter old password");
                } else if (Passnew.isEmpty()) {
                    passnew.setError("Enter new password");
                } else if (Passcon.isEmpty()) {
                    passcon.setError("Enter confirm password");
                } else if (!Passnew.equals(Passcon)) {
                    passcon.setError("Password not matched");
                } else {
                    sqLiteDatabase.execSQL("UPDATE user SET pass='" + Passnew + "' WHERE name='" + Nameold + "'");
                    nameold.setText("");
                    passnew.setText("");
                    passcon.setText("");
                    sqLiteDatabase.close();
                    finish();
                    Intent intent = new Intent(changepass.this, MainNavigationActivity.class);
                    startActivity(intent);
                    Toast.makeText(changepass.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}