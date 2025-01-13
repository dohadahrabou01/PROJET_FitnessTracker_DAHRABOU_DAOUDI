package com.example.fitnesstracker.Adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.GoalActivity;
import com.example.fitnesstracker.ProgressActivity;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Goal;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Adaptateur pour afficher une liste d'objectifs dans un RecyclerView.
 * Permet de gérer l'affichage, la modification, la progression et la suppression des objectifs.
 */
public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    /**
     * Liste des objectifs à afficher.
     */
    private ArrayList<Goal> goalList;
    /**
     * Contexte de l'application (activité actuelle).
     */
    private Context context;

    /**
     * Constructeur de l'adaptateur.
     *
     * @param context Contexte de l'activité dans laquelle cet adaptateur est utilisé.
     * @param goalList Liste des objectifs à afficher dans la RecyclerView.
     */
    public GoalAdapter(Context context, ArrayList<Goal> goalList) {
        this.context = context;
        this.goalList = goalList;
    }

    /**
     * Crée une nouvelle vue pour chaque élément de la liste.
     * Cette méthode est appelée pour gonfler la vue d'un élément lorsqu'il est nécessaire.
     *
     * @param parent Le parent dans lequel la vue sera insérée.
     * @param viewType Le type de la vue à créer (pas utilisé ici).
     * @return Un nouvel {@link GoalViewHolder} qui contient la vue de l'élément.
     */
    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    /**
     * Associe les données de l'objectif à la vue d'un élément particulier.
     * Cette méthode est appelée pour chaque élément dans la RecyclerView.
     *
     * @param holder Le {@link GoalViewHolder} qui contient la vue de l'élément.
     * @param position La position de l'élément dans la liste.
     */
    @Override
    public void onBindViewHolder(GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);

        // Définir les valeurs des TextViews
        holder.textViewGoalType.setText("Objectif : " + goal.getGoalType());
        holder.textViewTargetValue.setText("Cible : " + goal.getTargetValue());
        holder.textViewStartDate.setText("Début : " + goal.getStartDate());
        holder.textViewEndDate.setText("Fin : " + goal.getEndDate());
        holder.textViewStatus.setText("Statut : " + goal.getStatus());

        // Afficher ou masquer le bouton "Faire progresser" selon le statut
        if ("En cours".equals(goal.getStatus().trim())) {
            holder.buttonProgressGoal.setVisibility(View.VISIBLE);
        } else {
            holder.buttonProgressGoal.setVisibility(View.GONE);
        }

        // Ouvrir le DatePickerDialog pour sélectionner la date de début
        holder.textViewStartDate.setOnClickListener(v -> showDatePickerDialog(goal, "start"));

        // Ouvrir le DatePickerDialog pour sélectionner la date de fin
        holder.textViewEndDate.setOnClickListener(v -> showDatePickerDialog(goal, "end"));

        // Gestion du bouton "Faire progresser"
        holder.buttonProgressGoal.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProgressActivity.class);
            intent.putExtra("goalId", goal.getGoalId()); // Passer l'ID de l'objectif
            context.startActivity(intent);
        });

        // Gestion du bouton "Éditer"
        holder.buttonEditGoal.setOnClickListener(v -> {
            Intent intent = new Intent(context, GoalActivity.class);
            intent.putExtra("goal_id", goal.getGoalId()); // Passer l'ID de l'objectif
            context.startActivity(intent);
        });

        // Gestion du bouton "Supprimer"
        holder.buttonDeleteGoal.setOnClickListener(v -> {
            deleteGoal(goal.getGoalId(), position);
        });
    }

    /**
     * Retourne le nombre d'éléments dans la liste des objectifs.
     *
     * @return Le nombre d'objectifs dans la liste.
     */
    @Override
    public int getItemCount() {
        return goalList.size();
    }

    /**
     * Affiche un {@link DatePickerDialog} pour sélectionner une date de début ou de fin pour un objectif.
     *
     * @param goal L'objectif dont la date de début ou de fin doit être mise à jour.
     * @param dateType Le type de date à mettre à jour, soit "start" pour la date de début, soit "end" pour la date de fin.
     */
    private void showDatePickerDialog(Goal goal, String dateType) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;

                    if ("start".equals(dateType)) {
                        goal.setStartDate(selectedDate);  // Mettre à jour la date de début
                    } else if ("end".equals(dateType)) {
                        goal.setEndDate(selectedDate);  // Mettre à jour la date de fin
                    }

                    // Mettre à jour l'affichage des dates
                    notifyDataSetChanged();
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }

    /**
     * Supprime un objectif de la base de données et de la liste d'objectifs.
     *
     * @param goalId L'ID de l'objectif à supprimer.
     * @param position La position de l'objectif dans la liste d'objectifs.
     */
    private void deleteGoal(int goalId, int position) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        int rowsDeleted = dbHelper.deleteGoal(goalId);

        if (rowsDeleted > 0) {
            goalList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Objectif supprimé", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Échec de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder pour contenir la vue d'un objectif dans la RecyclerView.
     * Permet de lier les vues de chaque élément de la liste avec leurs références.
     */
    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGoalType, textViewTargetValue, textViewStartDate, textViewEndDate, textViewStatus;
        Button buttonEditGoal, buttonDeleteGoal, buttonProgressGoal;

        /**
         * Constructeur du ViewHolder.
         * Liaison des vues de l'élément de la liste avec les IDs correspondants dans le layout XML.
         *
         * @param itemView La vue de l'élément de l'objectif.
         */
        public GoalViewHolder(View itemView) {
            super(itemView);

            textViewGoalType = itemView.findViewById(R.id.textViewGoalType);
            textViewTargetValue = itemView.findViewById(R.id.textViewTargetValue);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);

            buttonEditGoal = itemView.findViewById(R.id.buttonEditGoal);
            buttonDeleteGoal = itemView.findViewById(R.id.buttonDeleteGoal);
            buttonProgressGoal = itemView.findViewById(R.id.buttonProgressGoal);
        }
    }
}
