package com.example.mobile_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class LostFragment extends Fragment {

    Button btn;

    Spinner spinnerRegion;
    Spinner spinnerVille;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost, container, false);

        spinnerRegion = view.findViewById(R.id.region_spinner);
        spinnerVille = view.findViewById(R.id.ville_spinner);

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

        return view;
    }
}