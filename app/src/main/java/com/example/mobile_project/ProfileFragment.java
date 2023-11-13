package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {
    ImageView profileImageView;
    TextInputEditText usernameEditText, emailEditText, phoneEditText, passwordEditText;
    Button saveChangesButton;
    SessionManager sessionManager;
    AppDatabase database;
    String newPhotoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            String newPhotoPath = saveImageToInternalStorage(selectedImageUri);

            this.newPhotoPath = newPhotoPath;
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        String newPhotoPath = null;
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);

            if (inputStream != null) {
                String filename = "profile_image_" + System.currentTimeMillis() + ".jpg";

                FileOutputStream outputStream = requireContext().openFileOutput(filename, Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                newPhotoPath = requireContext().getFileStreamPath(filename).getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newPhotoPath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        profileImageView = view.findViewById(R.id.profileImageView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        saveChangesButton = view.findViewById(R.id.saveChangesButton);

        sessionManager = new SessionManager(requireContext());

        String username = sessionManager.getUserName();
        String email = sessionManager.getEmail();
        String phone = sessionManager.getPhone();
        String password = sessionManager.getPassword();
        String photoPath = sessionManager.getPhotoUrl();

        usernameEditText.setText(username);
        emailEditText.setText(email);
        phoneEditText.setText(phone);
        passwordEditText.setText(password);

        Button changeProfilePictureButton = view.findViewById(R.id.changeProfileImageButton);

        changeProfilePictureButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        if (photoPath != null) {
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                profileImageView.setImageBitmap(bitmap);
            }
        }

        saveChangesButton.setOnClickListener(v -> {
            ioThread(() -> {
                String updatedUsername = usernameEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedPhone = phoneEditText.getText().toString();
                String updatedPassword = passwordEditText.getText().toString();

                sessionManager.updateUserInfo(sessionManager.getUserId(), updatedUsername, updatedEmail, updatedPhone, updatedPassword);

                User updatedUser = new User();
                updatedUser.setId(sessionManager.getUserId());
                updatedUser.setUserName(updatedUsername);
                updatedUser.setEmail(updatedEmail);
                updatedUser.setPassword(updatedPassword);
                updatedUser.setPhone(Integer.parseInt(updatedPhone));

                if (newPhotoPath != null) {
                    updatedUser.setPhoto(newPhotoPath);

                    sessionManager.updateProfileImage(newPhotoPath);
                } else {
                    updatedUser.setPhoto(sessionManager.getPhotoUrl());
                }

                database.userDao().updateUser(updatedUser);

                getActivity().runOnUiThread(() -> {
                    CharSequence text = "Profile information updated!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(requireContext(), text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            });
        });

        return view;
    }
}