package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;

import java.util.List;

public class MainActivity4 extends AppCompatActivity {

    AppDatabase database;
    TextView login;

    EditText username, phone, email, password;

    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        database = AppDatabase.getAppDatabase(getApplicationContext());

        signup = findViewById(R.id.btnSignUp);
        username = findViewById(R.id.etFullName);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);

        signup.setOnClickListener(view -> {
            ioThread(() -> {
                if (database.userDao().login(username.getText().toString(), email.getText().toString())) {
                    runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Le nom d'utilisateur ou l'e-mail existe déjà. Veuillez en choisir un autre.")
                                .setTitle("Alerte")
                                .setPositiveButton("OK", (dialog, which) -> {
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    });
                } else {
                    User user = new User(username.getText().toString(), Integer.parseInt(phone.getText().toString()), email.getText().toString(), password.getText().toString(), "aaaaaa");
                    database.userDao().insertUser(user);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(MainActivity4.this, MainActivity2.class);
                        startActivity(intent);
                    });
                }
            });
        });

        // Go to login
        login = findViewById(R.id.signin);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        });
    }
}
