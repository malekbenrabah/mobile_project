package com.example.mobile_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.session.SessionManager;

public class MainActivity3 extends AppCompatActivity {

    public static String spName = "sharedPref";
    public SharedPreferences sp;
    AppDatabase database;
    Button login;
    EditText loginEt, pwdEt;
    TextView register;
    CheckBox rememberMe;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        database = AppDatabase.getAppDatabase(getApplicationContext());

        sessionManager = new SessionManager(this);

        sp = getSharedPreferences(spName, MODE_PRIVATE);

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            finish();
            return;
        }

        login = findViewById(R.id.btnLogin);
        loginEt = findViewById(R.id.etEmail);
        pwdEt = findViewById(R.id.etPassword);
        rememberMe = findViewById(R.id.rememberMe);

        // Use shared preferences to store username and password if checked
        if (rememberMe.isChecked()) {
            loginEt.setText(sp.getString("userName", ""));
            pwdEt.setText(sp.getString("pwd", ""));
        }

        login.setOnClickListener(view -> {
            String email = loginEt.getText().toString();
            String password = pwdEt.getText().toString();

            ioThread(() -> {
                User user = database.userDao().authenticate(email, password);
                if (user != null) {
                    sessionManager.createSession(user.getId(), user.getUserName(), email, String.valueOf(user.getPhone()), user.getPassword(), user.getPhoto(), user.isAdmin());
                    runOnUiThread(() -> {
                        if (rememberMe.isChecked()) {
                            // Store the username and password in shared preferences
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userName", user.getUserName());
                            editor.putString("pwd", password);
                            editor.apply();
                        }

                        Intent intent = new Intent(this, MainActivity2.class);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Email or password is incorrect.")
                                .setTitle("Invalid Credentials")
                                .setPositiveButton("OK", (dialog, which) -> {
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    });
                }
            });
        });

        register = findViewById(R.id.tvOr);
        register.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
        });
    }

    private void ioThread(Runnable runnable) {
        AppDatabase.ioThread(runnable);
    }
}
