package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity4 extends AppCompatActivity {

    AppDatabase database;
    TextView login;

    EditText username, phone, email, password;

    Button signup;

    SessionManager sessionManager;

    FloatingActionButton addImage;
    CircleImageView photo;
    String photoPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                photoPath = saveToInternalStorage(bitmap);
                photo.setImageURI(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mypath.getAbsolutePath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        sessionManager = new SessionManager(this);

        database = AppDatabase.getAppDatabase(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            finish();
            return;
        }

        signup = findViewById(R.id.btnSignUp);
        username = findViewById(R.id.etFullName);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);

        photo = findViewById(R.id.ciAvatar);
        addImage = findViewById(R.id.selectPhoto);

        addImage.setOnClickListener(view -> ImagePicker.with(MainActivity4.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        signup.setOnClickListener(view -> {
            ioThread(() -> {
                if (database.userDao().authenticate(username.getText().toString(), email.getText().toString()) != null) {
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
                    String imagePath = photoPath;

                    User user = new User(username.getText().toString(), Integer.parseInt(phone.getText().toString()), email.getText().toString(), password.getText().toString(), imagePath, User.Role.CLIENT);
                    database.userDao().insertUser(user);
                    sessionManager.createSession(user.getId(), username.getText().toString(), email.getText().toString(), phone.getText().toString(), password.getText().toString(), imagePath, user.isAdmin());

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
