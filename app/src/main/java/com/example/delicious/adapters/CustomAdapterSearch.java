package com.example.delicious.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicious.R;
import com.example.delicious.models.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class CustomAdapterSearch extends RecyclerView.Adapter<CustomAdapterSearch.MyViewHolder> {

    private ArrayList<Recipe> dataset;
    private Context context;

    public CustomAdapterSearch(ArrayList<Recipe> dataSet,Context context) {

        this.dataset = dataSet;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageViewRecipe;
        Button infoBtn;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewNameHpCard);
            imageViewRecipe = itemView.findViewById(R.id.imageViewHpCard);
            infoBtn = itemView.findViewById(R.id.infoBtnApi);


        }
    }

    @NonNull
    @Override
    public CustomAdapterSearch.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_home_page, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterSearch.MyViewHolder holder, int position) {
        if (dataset == null || dataset.isEmpty()) {
            return;
        }

        Recipe recipe = dataset.get(position);
        holder.textViewName.setText(recipe.getName());

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageViewRecipe);
        }
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show alert dialog with recipe information
                showRecipeInfoDialog(holder.itemView.getContext(), recipe); // Use holder.itemView.getContext()
            }
        });
    }

    private Bitmap decodeImageFromString(String imageString) {
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }
    private void showRecipeInfoDialog(Context context, Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);

        // Initialize views in the dialog layout
        TextView textViewRecipeTime = dialogView.findViewById(R.id.textViewTimeDialog);
        TextView textViewRecipeDetails = dialogView.findViewById(R.id.textViewDescriptionDialog);
        TextView textViewRecipeIngredients = dialogView.findViewById(R.id.textViewIngredientsDialog);
        TextView textViewRecipeInstructions = dialogView.findViewById(R.id.textViewInstructionsDialog);

        // Set recipe information to the dialog views
        textViewRecipeTime.setText(recipe.getPreparationTime());
        textViewRecipeDetails.setText(Html.fromHtml(recipe.getDescription()));
        textViewRecipeInstructions.setText(recipe.getSteps()); // Replace getDetails with actual method
        StringBuilder ingredientsStringBuilder = new StringBuilder();
        for (String ingredient : recipe.getIngredients()) {
            ingredientsStringBuilder.append("- ").append(ingredient).append("\n");
        }
        textViewRecipeIngredients.setText(ingredientsStringBuilder.toString());

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String recipeId = UUID.randomUUID().toString();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                        DatabaseReference recipesRef = userRef.child("recipes");
                        DatabaseReference fromApi = recipesRef.child("from api");
                        recipe.setId(recipeId);
                        fromApi.child(recipeId).setValue(recipe)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Recipe added successfully
                                        Log.d("success", "ok");
                                        Toast.makeText(context, "Recipe added successfully", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to add recipe
                                        Log.d("fail", ":(");
                                        Toast.makeText(context, "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                if (positiveButton != null && negativeButton != null) {
                    positiveButton.setTextColor(0xFFCC5E03);
                    negativeButton.setTextColor(0xFFCC5E03);
                }
            }
        });
        dialog.show();
    }
}
