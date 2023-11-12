package com.example.mobile_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobile_project.dao.UserDao;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity4 extends AppCompatActivity {

    AppDatabase database;
    TextView login;

    EditText username, phone, email, password;

    Button signup;

    FloatingActionButton addImage;
    CircleImageView photo;

    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        database = AppDatabase.getAppDatabase(getApplicationContext());


        signup = findViewById(R.id.btnSignUp);
        username = findViewById(R.id.etFullName);
        phone = findViewById(R.id.etPhone);
        email= findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);

        //image
        photo=findViewById(R.id.ciAvatar);
        addImage = findViewById(R.id.selectPhoto);


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity4.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });


        signup.setOnClickListener(view -> {

            boolean isValid = true;

            if (username.getText().toString().trim().isEmpty()) {
                setError("Le nom d'utilisateur est requis.", R.id.userNameError);
                isValid = false;
            }

            if (phone.getText().toString().trim().isEmpty()) {
                setError("Le numéro de téléphone est requis.", R.id.phoneError);
                isValid = false;
            } else if (phone.getText().toString().trim().length() != 8) {
                setError("Le numéro de téléphone doit avoir exactement 8 chiffres.", R.id.phoneError);
                isValid = false;
            }

            if (email.getText().toString().trim().isEmpty()) {
                setError("L'e-mail est requis.", R.id.emailError);
                isValid = false;
            }

            if (password.getText().toString().trim().isEmpty()) {
                setError("Le mot de passe est requis.", R.id.passwordError);
                isValid = false;
            }
            if(isValid) {
                if (database.userDao().existsUserName(username.getText().toString())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Le nom d'utilisateur existe déjà. Veuillez en choisir un autre.")
                            .setTitle("Alerte")
                            .setPositiveButton("OK", (dialog, which) -> {
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (database.userDao().existsEmail(email.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("L'email existe déjà. Veuillez en choisir un autre.")
                            .setTitle("Alerte")
                            .setPositiveButton("OK", (dialog, which) -> {
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    User user = new User();
                    user.setUserName(username.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPhone(Integer.parseInt(phone.getText().toString()));
                    user.setPassword(password.getText().toString());
                    user.setPhoto("aaaa");

                    database.userDao().insertUser(user);

                    List<User> users = database.userDao().getAll();
                    for (User u : users) {
                        System.out.println("user info : " + u);
                    }


                    Intent intent = new Intent(this, MainActivity2.class);
                    startActivity(intent);
                }
            }
        });

        //go to login
        login= findViewById(R.id.signin);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
        });
    }

    private void setError(String errorMessage, int textViewId) {
        TextView errorTextView = findViewById(textViewId);
        errorTextView.setText(errorMessage);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void clearErrors() {
        TextView userNameError = findViewById(R.id.userNameError);
        TextView phoneError = findViewById(R.id.phoneError);
        TextView emailError = findViewById(R.id.emailError);
        TextView passwordError = findViewById(R.id.passwordError);

        userNameError.setVisibility(View.GONE);
        phoneError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        photoPath = data.getStringExtra(ImagePicker.EXTRA_FILE_PATH);
        photo.setImageURI(uri);

    }
}