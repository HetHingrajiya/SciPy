package com.example.scipy;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText name, pass, email, contact;

    TextView tv2;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        DBHelper db = new DBHelper(RegisterActivity.this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.ed);
        contact = findViewById(R.id.contact);
        register = findViewById(R.id.register);
        tv2 = findViewById(R.id.tv2);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getText().toString();
                String Pass = pass.getText().toString();
                String Email = email.getText().toString();
                String Contact = contact.getText().toString();

                if (Name.isEmpty()) {
                    name.setError("enter name");
                } else if (Pass.isEmpty()) {
                    pass.setError("enter password");
                } else if (Pass.length() < 8) {
                    pass.setError("enter 8 digit password");
                } else if (Email.isEmpty()) {
                    email.setError("enter email");
                } else if (!Email.matches(EmailPattern)) {
                    email.setError("enter valid email");
                } else if (Contact.isEmpty()) {
                    contact.setError("enter contact");
                } else if (Contact.length() < 10) {
                    contact.setError("enter valid contact");
                }
                else {
                    sqLiteDatabase.execSQL("insert into user values(null,'" + Name + "','" + Pass + "','" + Email + "','" + Contact + "')");
                    name.setText("");
                    pass.setText("");
                    email.setText("");
                    contact.setText("");
                    Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }


            }

        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}