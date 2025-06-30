package com.example.scipy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    private TextInputEditText nameEdit, emailEdit, contactEdit, passwordEdit, confirmPasswordEdit;
    private TextInputLayout confirmPasswordLayout;
    private MaterialButton editProfileBtn, updateProfileBtn, logoutBtn;

    private SharedPreferences sp;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize SharedPreferences
        sp = requireContext().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);

        // Initialize views
        nameEdit = view.findViewById(R.id.name);
        emailEdit = view.findViewById(R.id.email);
        contactEdit = view.findViewById(R.id.contact);
        passwordEdit = view.findViewById(R.id.pass);
        confirmPasswordEdit = view.findViewById(R.id.conpass);
        confirmPasswordLayout = view.findViewById(R.id.conpass_layout);

        editProfileBtn = view.findViewById(R.id.editprofile);
        updateProfileBtn = view.findViewById(R.id.upprofile);
        logoutBtn = view.findViewById(R.id.logout);

        // Load data from SharedPreferences
        loadProfileData();

        // Disable editing initially
        setInputsEnabled(false);

        editProfileBtn.setOnClickListener(v -> {
            setInputsEnabled(true);
            confirmPasswordLayout.setVisibility(View.VISIBLE);
            editProfileBtn.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        });

        updateProfileBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = nameEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String contact = contactEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();

                // Save to SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(ConstantSp.name, name);
                editor.putString(ConstantSp.email, email);
                editor.putString(ConstantSp.contact, contact);
                editor.putString(ConstantSp.password, password);
                editor.apply();

                // Save to SQLite DB
                DBHelper dbHelper = new DBHelper(requireContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("email", email);
                values.put("contact", contact);
                values.put("pass", password);

                // Get user ID from shared preferences
                String userId = sp.getString(ConstantSp.userid, "0");

                long result = db.update("user", values, "userid = ?", new String[]{userId});

                db.close();

                if (result != -1) {
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                }

                // UI adjustments
                setInputsEnabled(false);
                confirmPasswordLayout.setVisibility(View.GONE);
                editProfileBtn.setVisibility(View.VISIBLE);
                updateProfileBtn.setVisibility(View.GONE);
            }
        });

        logoutBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear(); // Clear all saved data
            editor.apply();

            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();

            // Redirect to MainActivity (you can change it to LoginActivity if needed)
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void setInputsEnabled(boolean enabled) {
        nameEdit.setEnabled(enabled);
        emailEdit.setEnabled(enabled);
        contactEdit.setEnabled(enabled);
        passwordEdit.setEnabled(enabled);
        confirmPasswordEdit.setEnabled(enabled);

        if (!enabled) {
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private boolean validateInputs() {
        String name = nameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String contact = contactEdit.getText().toString().trim();
        String pass = passwordEdit.getText().toString().trim();
        String conPass = confirmPasswordEdit.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || pass.isEmpty() || conPass.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(conPass)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loadProfileData() {
        nameEdit.setText(sp.getString(ConstantSp.name, ""));
        emailEdit.setText(sp.getString(ConstantSp.email, ""));
        contactEdit.setText(sp.getString(ConstantSp.contact, ""));
        passwordEdit.setText(sp.getString(ConstantSp.password, ""));
        confirmPasswordEdit.setText(sp.getString(ConstantSp.password, ""));
    }
}
