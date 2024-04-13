package com.example.delicious.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.delicious.MainActivity;
import com.example.delicious.R;
import com.example.delicious.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRecipeStep1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRecipeStep1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateRecipeStep1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateRecipe1.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRecipeStep1 newInstance(String param1, String param2) {
        CreateRecipeStep1 fragment = new CreateRecipeStep1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_recipe_step1, container, false);
        Recipe currentRecipe = MainActivity.currentRecipe;
        Spinner spinner = view.findViewById(R.id.spinnerCategory);
// Define your options
        String[] options = {"main course",
                "side dish",
                "dessert",
                "appetizer",
                "salad",
                "bread",
                "breakfast",
                "soup",
                "beverage",
                "sauce",
                "marinade",
                "fingerfood",
                "snack",
                "drink"};
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Button toStep2 = view.findViewById(R.id.NextToStep4Btn);
        Button backToHomePage = view.findViewById(R.id.backToHomePageFromSearchBtn);
        toStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = view.findViewById(R.id.editTextName);
                EditText editTextTime = view.findViewById(R.id.editTextTime);
                EditText editTextDescription = view.findViewById(R.id.editTextMultilineDescription);
                Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);

                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();
                String time = editTextTime.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();

                currentRecipe.setName(name);
                currentRecipe.setCategory(category);
                currentRecipe.setPreparationTime(time);
                currentRecipe.setDescription(description);
                MainActivity.currentRecipe= currentRecipe;
                Navigation.findNavController(view).navigate(R.id.action_createRecipe1_to_createRecipeStep2);
            }
        });
         backToHomePage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Navigation.findNavController(view).navigate(R.id.action_createRecipe1_to_login2);
             }
         });
        return view;
    }
}