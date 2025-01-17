package com.example.delicious.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.delicious.activity.MainActivity;
import com.example.delicious.R;
import com.example.delicious.models.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRecipeStep3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRecipeStep3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateRecipeStep3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateRecipeStep33.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRecipeStep3 newInstance(String param1, String param2) {
        CreateRecipeStep3 fragment = new CreateRecipeStep3();
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
        View view = inflater.inflate(R.layout.fragment_create_recipe_step3, container, false);
        Recipe currentRecipe = MainActivity.currentRecipe;
        Button toStep4 = view.findViewById(R.id.NextToStep4Btn);
        Button backToStep2 = view.findViewById(R.id.logoutBtnStep1);
        EditText editTextSteps = view.findViewById(R.id.editTextMultiLineSteps);
        backToStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_createRecipeStep3_to_createRecipeStep2);
            }

        });
        toStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String steps = editTextSteps.getText().toString();
                if (steps.isEmpty()) {
                    Toast.makeText(getContext(), "steps must be filled", Toast.LENGTH_SHORT).show();
                } else {
                    Recipe currentRecipe = MainActivity.currentRecipe;
                    currentRecipe.setSteps(steps);
                    Navigation.findNavController(view).navigate(R.id.action_createRecipeStep3_to_createRecipe);
                }
            }
        });

        return view;
    }
}