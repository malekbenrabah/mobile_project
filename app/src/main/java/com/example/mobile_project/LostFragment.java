package com.example.mobile_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LostFragment extends Fragment {

    Button btn;

    Spinner spinnerRegion;
    Spinner spinnerVille;

    ImageButton addImage;

    TextView titreError;
    EditText titre,description;

    private SharedPreferences sp;

    AppDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost, container, false);

        sp = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        spinnerRegion = view.findViewById(R.id.region_spinner);
        spinnerVille = view.findViewById(R.id.ville_spinner);

        addImage = view.findViewById(R.id.imageButton);

        titre = view.findViewById(R.id.titre);
        titreError = view.findViewById(R.id.titreError);

        description = view.findViewById(R.id.editTextTextMultiLine2);



        //photo

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(LostFragment.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });

        // remplir list region dans region spinner
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.regions_array, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // region selectionnee
                String selectedRegion = adapterView.getItemAtPosition(i).toString();

                // Load the appropriate cities/villes array based on the selected region
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

            String selectedRegion = spinnerRegion.getSelectedItem().toString();
            String selectedVille = spinnerVille.getSelectedItem().toString();

            boolean isValid = true;

            if (titre.getText().toString().trim().isEmpty()) {
                titreError.setText("Le titre est requis");
                isValid = false;
            }

            if(isValid){
                int userId = sp.getInt("userId", -1);
                String typePost = sp.getString("typePost","");
                String current_Date=getCurrentDate();
                System.out.println("userId: "+userId+" current_date : "+current_Date+" type post :"+ typePost);

                Post post = new Post();
                post.setTitle(titre.getText().toString());
                post.setDescription(description.getText().toString());
                post.setUserId(userId);
                post.setPhoto("testphoto");
                post.setCreated_at(current_Date);
                post.setVille(selectedVille);
                post.setRegion(selectedRegion);
                post.setPost_type(typePost);

                database.postDao().insertPost(post);
                List<Post> posts = database.postDao().getAll();
                for (Post p : posts) {
                    System.out.println("post info : " + p);
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).commit();

            }


        });




        return view;
    }

    public String getCurrentDate(){

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String formattedDate = sdf.format(currentDate);

        Log.d("FormattedDate", formattedDate);

        return formattedDate;
    }


}