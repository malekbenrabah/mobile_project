package com.example.mobile_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobile_project.database.AppDatabase;

public class MainActivity3 extends AppCompatActivity {

    TextView register;

    AppDatabase database;
    Button login;
    EditText loginEt, pwdEt;

    CheckBox rememberMe;

    public SharedPreferences sp;
    public static String spName = "sharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        database = AppDatabase.getAppDatabase(getApplicationContext());


        sp = getSharedPreferences(spName,MODE_PRIVATE);


        login = findViewById(R.id.btnLogin);
        loginEt = findViewById(R.id.etEmail);
        pwdEt = findViewById(R.id.etPassword);
        rememberMe = findViewById(R.id.rememberMe);

        loginEt.setText(sp.getString("userName",""));
        pwdEt.setText(sp.getString("pwd",""));

        login.setOnClickListener(view -> {

            if(database.userDao().login(loginEt.getText().toString(), pwdEt.getText().toString())) {

                if (rememberMe.isChecked()) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userName",loginEt.getText().toString());
                    editor.putString("pwd",pwdEt.getText().toString());
                    editor.commit();
                }

                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);


            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Nom d'utilisateur ou mot de passe incorrect.")
                        .setTitle("Informations d'identification invalides")
                        .setPositiveButton("OK", (dialog, which) -> {
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        //go to register
        register= findViewById(R.id.tvOr);
        register.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity4.class);
            startActivity(intent);
        });
    }
}