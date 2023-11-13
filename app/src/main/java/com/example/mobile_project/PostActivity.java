package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    Button btn;

    Spinner spinnerRegion;
    Spinner spinnerVille;

    ImageButton addImage;

    TextView titreError;
    EditText titre, description;

    private SharedPreferences sp;

    AppDatabase database;

    String photoPath;

    ImageView photo;

    FloatingActionButton addBtn;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        database = AppDatabase.getAppDatabase(getApplicationContext());
        sp = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        spinnerRegion = findViewById(R.id.region_spinner);
        spinnerVille = findViewById(R.id.ville_spinner);

        addImage = findViewById(R.id.imageButton);

        titre = findViewById(R.id.etTitre);
        titreError = findViewById(R.id.titreError);

        description = findViewById(R.id.etDescription);

        photo = findViewById(R.id.photo_ad);




        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(PostActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        // remplir list region dans region spinner
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.regions_array, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // region selectionnee
                String selectedRegion = adapterView.getItemAtPosition(i).toString();

                // chargement villes en fonction des region
                int citiesArrayId = getResources().getIdentifier(selectedRegion.toLowerCase() + "_villes_array", "array", getPackageName());
                ArrayAdapter<CharSequence> villeAdapter = ArrayAdapter.createFromResource(getApplicationContext(), citiesArrayId, android.R.layout.simple_spinner_item);
                villeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerVille.setAdapter(villeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(view -> {
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

                    runOnUiThread(() -> {
                        Intent intent = new Intent(PostActivity.this, MainActivity2.class);
                        startActivity(intent);
                    });
                }
            });
        });

        addBtn = findViewById(R.id.fabAdd);
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("fragmentToLoad", "PostFragment");
            startActivity(intent);
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent intent = new Intent(this, MainActivity2.class);
                intent.putExtra("fragmentToLoad", "HomeFragment");
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.posts) {
                Intent intent = new Intent(this, MainActivity2.class);
                intent.putExtra("fragmentToLoad", "AllUserPostsFragment");
                startActivity(intent);
                return true;
            }
            return false;
        });



    }

    public String getCurrentDate() {

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String formattedDate = sdf.format(currentDate);

        Log.d("FormattedDate", formattedDate);

        return formattedDate;
    }

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


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }


}

