package com.example.mobile_project;


import static android.app.Activity.RESULT_OK;
import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.github.dhaval2404.imagepicker.ImagePicker;

import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.fragment.app.Fragment;

public class LostFragment extends Fragment {

    Button btn;

    Spinner spinnerRegion;
    Spinner spinnerVille;

    ImageButton addImage;

    TextView titreError;
    EditText titre,description;

    private SharedPreferences sp;

    AppDatabase database;

    String photoPath;

    //photo
    Uri imageUri;

    ImageView photo;


    private Uri selectedImageUri;

    ActivityResultLauncher<Intent> mGetContent;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_lost, container, false);

        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        sp = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        spinnerRegion = view.findViewById(R.id.region_spinner);
        spinnerVille = view.findViewById(R.id.ville_spinner);

        addImage = view.findViewById(R.id.imageButton);

        titre = view.findViewById(R.id.titre);
        titreError = view.findViewById(R.id.titreError);

        description = view.findViewById(R.id.editTextTextMultiLine2);

        photo = view.findViewById(R.id.photo_ad);


        addImage.setOnClickListener(view12 -> ImagePicker.with(LostFragment.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start());


        // remplir list region dans region spinner
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.regions_array, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // region selectionnee
                String selectedRegion = adapterView.getItemAtPosition(i).toString();

                // chargement villes en fonction des region
                int citiesArrayId = getResources().getIdentifier(selectedRegion.toLowerCase() + "_villes_array", "array", requireContext().getPackageName());
                ArrayAdapter<CharSequence> villeAdapter = ArrayAdapter.createFromResource(requireContext(), citiesArrayId, android.R.layout.simple_spinner_item);
                villeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVille.setAdapter(villeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn = view.findViewById(R.id.add);
        btn.setOnClickListener(view1 -> {
            ioThread(() -> {
                String selectedRegion = spinnerRegion.getSelectedItem().toString();
                String selectedVille = spinnerVille.getSelectedItem().toString();

                boolean isValid = true;

                if (titre.getText().toString().trim().isEmpty()) {
                    titreError.setText("Le titre est requis");
                    isValid = false;
                }

                if (isValid) {

                        int userId = sp.getInt("userId", -1);
                        String typePost = sp.getString("typePost", "");
                        String current_Date = getCurrentDate();
                        System.out.println("userId: " + userId + " current_date : " + current_Date + " type post :" + typePost);

                        Post post = new Post();
                        post.setTitle(titre.getText().toString());
                        post.setDescription(description.getText().toString());
                        post.setUserId(userId);
                        post.setPhoto(photoPath);
                        post.setCreated_at(current_Date);
                        post.setVille(selectedVille);
                        post.setRegion(selectedRegion);
                        post.setPost_type(typePost);

                        database.postDao().insertPost(post);
                        List<Post> posts = database.postDao().getAll();
                        for (Post p : posts) {
                            System.out.println("post info : " + p);
                        }
                    getActivity().runOnUiThread(() -> {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
                    });
                }

            });



        });

        return view;
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mGetContent.launch(intent);
    }


    public String getCurrentDate(){

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String formattedDate = sdf.format(currentDate);

        Log.d("FormattedDate", formattedDate);

        return formattedDate;
    }

    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        cursor.close();
        return imagePath;
    }

    /*
    private void saveImageToInternalStorage(String imagePath) {
        // Load the selected image as a Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // Save the bitmap to internal storage
        ContextWrapper cw = new ContextWrapper(requireContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, "UniqueFileName" + ".jpg");
        if (!file.exists()) {
            Log.d("path", file.toString());
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                // Now 'file' contains the saved image
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

     */

    //photo
    private boolean saveImageExternalStorage(String imgName, Bitmap bmp){
        Uri imageCollection = null;
        ContentResolver resolver = getContext().getContentResolver();
        imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        Uri imageUri= resolver.insert(imageCollection,contentValues);
        try {
            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bmp.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
            Objects.requireNonNull(outputStream);
            return true;
        }catch (Exception e){
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    //save img
    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
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












}