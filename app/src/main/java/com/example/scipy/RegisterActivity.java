package com.example.scipy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name, pass, email, contact;
    TextView tv2;
    Button register;

    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    DBHelper db;
    SharedPreferences sp;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
        sqLiteDatabase = db.getWritableDatabase();
        sp = getSharedPreferences(ConstantSp.pref, MODE_PRIVATE);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.ed);
        contact = findViewById(R.id.contact);
        register = findViewById(R.id.register);
        tv2 = findViewById(R.id.tv2);

        register.setOnClickListener(view -> {
            String Name = name.getText().toString().trim();
            String Pass = pass.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Contact = contact.getText().toString().trim();

            if (Name.isEmpty()) {
                name.setError("Enter name");
            } else if (Pass.isEmpty()) {
                pass.setError("Enter password");
            } else if (Pass.length() < 8) {
                pass.setError("Enter 8 digit password");
            } else if (Email.isEmpty()) {
                email.setError("Enter email");
            } else if (!Email.matches(EmailPattern)) {
                email.setError("Enter valid email");
            } else if (Contact.isEmpty()) {
                contact.setError("Enter contact");
            } else if (Contact.length() < 10) {
                contact.setError("Enter valid contact");
            } else {
                sqLiteDatabase.execSQL("insert into user values(null,'" + Name + "','" + Pass + "','" + Email + "','" + Contact + "')");
                name.setText("");
                pass.setText("");
                email.setText("");
                contact.setText("");
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tv2.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
