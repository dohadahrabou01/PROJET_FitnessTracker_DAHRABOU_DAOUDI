package com.example.fitnesstracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.TrainingActivity;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Training;

import java.util.ArrayList;

/**
 * Adapter pour afficher la liste des entraînements dans un RecyclerView.
 * Chaque élément de la liste représente un entraînement avec des informations telles que la durée,
 * la distance, les calories et l'heure de début/fin.
 */
public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder> {

    private ArrayList<Training> trainingList;  // Liste des entraînements
    private Context context;  // Contexte de l'application

    /**
     * Constructeur de l'adaptateur.
     *
     * @param trainingList Liste des entraînements à afficher.
     * @param context Contexte de l'application.
     */
    public TrainingAdapter(ArrayList<Training> trainingList, Context context) {
        this.trainingList = trainingList;
        this.context = context;
    }

    /**
     * Crée la vue pour chaque élément de la liste.
     *
     * @param parent Le parent dans lequel la vue sera ajoutée.
     * @param viewType Type de vue (utilisé pour gérer différents types de vues dans le RecyclerView).
     * @return Un nouveau ViewHolder qui contient la vue d'un élément de la liste.
     */
    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_training, parent, false);
        return new TrainingViewHolder(view);
    }

    /**
     * Lier les données de l'entraînement avec les vues dans le ViewHolder.
     *
     * @param holder Le ViewHolder qui contient les vues de l'élément.
     * @param position La position de l'élément dans la liste.
     */
    @Override
    public void onBindViewHolder(TrainingViewHolder holder, int position) {
        Training training = trainingList.get(position);
        holder.durationTextView.setText("Durée : " + training.getDuration() + " minutes");
        holder.distanceTextView.setText("Distance : " + training.getDistance() + " km");
        holder.caloriesTextView.setText("Calories : " + training.getCalories());
        holder.startTimeTextView.setText("Début : " + training.getStartTime());
        holder.endTimeTextView.setText("Fin : " + training.getEndTime());

        // Gérer l'action du bouton "Éditer"
        holder.editButton.setOnClickListener(v -> {
            // Ouvrir l'écran d'édition pour cet entraînement
            Intent intent = new Intent(context, TrainingActivity.class);
            intent.putExtra("training_id", training.getWorkoutId()); // Passer l'ID de l'entraînement à modifier
            context.startActivity(intent);
        });

        // Gérer l'action du bouton "Supprimer"
        holder.deleteButton.setOnClickListener(v -> {
            // Supprimer l'entraînement de la base de données
            deleteTraining(training.getWorkoutId(), position);
        });
    }

    /**
     * Retourne le nombre d'éléments dans la liste.
     *
     * @return Le nombre d'éléments dans la liste des entraînements.
     */
    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    /**
     * Supprime un entraînement de la liste et de la base de données.
     *
     * @param workoutId L'ID de l'entraînement à supprimer.
     * @param position La position de l'élément à supprimer dans la liste.
     */
    private void deleteTraining(int workoutId, int position) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        int rowsDeleted = dbHelper.deleteTraining(workoutId); // Suppression de l'entraînement par ID

        if (rowsDeleted > 0) {
            // Suppression réussie
            Toast.makeText(context, "Entraînement supprimé", Toast.LENGTH_SHORT).show();
            // Retirer l'élément de la liste et notifier l'adaptateur
            trainingList.remove(position);
            notifyItemRemoved(position);
        } else {
            // Si une erreur se produit lors de la suppression
            Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder pour afficher un entraînement dans la liste.
     */
    public static class TrainingViewHolder extends RecyclerView.ViewHolder {
        TextView durationTextView, distanceTextView, caloriesTextView, startTimeTextView, endTimeTextView;
        Button editButton, deleteButton;

        /**
         * Constructeur de la classe ViewHolder.
         *
         * @param itemView La vue de l'élément à afficher.
         */
        public TrainingViewHolder(View itemView) {
            super(itemView);
            // Initialisation des vues
            durationTextView = itemView.findViewById(R.id.textViewDuration);
            distanceTextView = itemView.findViewById(R.id.textViewDistance);
            caloriesTextView = itemView.findViewById(R.id.textViewCalories);
            startTimeTextView = itemView.findViewById(R.id.textViewStartTime);
            endTimeTextView = itemView.findViewById(R.id.textViewEndTime);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
