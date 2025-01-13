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
import com.example.fitnesstracker.AddProgressActivity;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Progress;

import java.util.ArrayList;

/**
 * Adapter pour afficher une liste de progrès dans un RecyclerView.
 * <p>
 * Cette classe est utilisée pour lier une liste d'objets `Progress` à un RecyclerView, chaque élément représentant un progrès avec une valeur, une date, et des options pour l'éditer ou le supprimer.
 * </p>
 */
public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {

    /** Liste des progrès à afficher */
    private ArrayList<Progress> progressList;

    /** Contexte de l'application, utilisé pour les actions de l'interface utilisateur */
    private Context context;

    /**
     * Constructeur de l'adaptateur.
     *
     * @param progressList Liste des progrès à afficher
     * @param context Contexte dans lequel l'adaptateur est utilisé
     */
    public ProgressAdapter(ArrayList<Progress> progressList, Context context) {
        this.progressList = (progressList != null) ? progressList : new ArrayList<>();
        this.context = context;
    }

    /**
     * Crée et retourne un ViewHolder pour un élément de la liste.
     *
     * @param parent Vue parent dans laquelle l'élément sera inséré
     * @param viewType Type de vue (non utilisé ici)
     * @return Un `ProgressViewHolder` contenant la vue pour un élément de la liste
     */
    @Override
    public ProgressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Gonfler la vue de l'élément de la liste
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    /**
     * Lie les données d'un objet `Progress` à un `ProgressViewHolder`.
     *
     * @param holder Le `ProgressViewHolder` auquel les données seront liées
     * @param position La position de l'élément dans la liste
     */
    @Override
    public void onBindViewHolder(ProgressViewHolder holder, int position) {
        Progress progress = progressList.get(position);

        // Remplir les TextView avec les valeurs de l'objet Progress
        holder.textProgressValue.setText("Valeur : " + progress.getCurrentValue());
        holder.textProgressDate.setText("Date : " + progress.getDate());

        // Action sur le bouton "Éditer"
        holder.editButton.setOnClickListener(v -> {
            // Ouvrir l'activité d'ajout/édition de progrès
            Intent intent = new Intent(context, AddProgressActivity.class);
            intent.putExtra("progress_id", progress.getId());  // Passer l'ID du progrès à modifier
            intent.putExtra("goalId", progress.getGoalId());   // Passer l'ID de l'objectif
            context.startActivity(intent);
        });

        // Action sur le bouton "Supprimer"
        holder.deleteButton.setOnClickListener(v -> {
            // Supprimer le progrès de la base de données
            deleteProgress(progress.getId(), position);
        });
    }

    /**
     * Supprime un progrès de la base de données et de la liste.
     *
     * @param progressId ID du progrès à supprimer
     * @param position Position de l'élément à supprimer dans la liste
     */
    private void deleteProgress(int progressId, int position) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        int rowsDeleted = dbHelper.deleteProgress(progressId); // Suppression du progrès par ID

        if (rowsDeleted > 0) {
            // Suppression réussie
            Toast.makeText(context, "Progrès supprimé", Toast.LENGTH_SHORT).show();
            // Retirer l'élément de la liste et notifier l'adaptateur
            progressList.remove(position);
            notifyItemRemoved(position);
        } else {
            // Erreur de suppression
            Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Retourne le nombre d'éléments dans la liste des progrès.
     *
     * @return Le nombre d'éléments dans la liste des progrès
     */
    @Override
    public int getItemCount() {
        return progressList.size();
    }

    /**
     * ViewHolder représentant un élément de la liste des progrès.
     * <p>
     * Contient les vues pour afficher la valeur du progrès, la date, et les boutons d'édition et de suppression.
     * </p>
     */
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        /** TextView affichant la valeur du progrès */
        TextView textProgressValue;

        /** TextView affichant la date du progrès */
        TextView textProgressDate;

        /** Bouton pour éditer un progrès */
        Button editButton;

        /** Bouton pour supprimer un progrès */
        Button deleteButton;

        /**
         * Constructeur pour initialiser le ViewHolder avec les vues associées.
         *
         * @param itemView Vue représentant un élément de la liste
         */
        public ProgressViewHolder(View itemView) {
            super(itemView);
            // Initialisation des vues à partir du layout
            textProgressValue = itemView.findViewById(R.id.textProgressValue);
            textProgressDate = itemView.findViewById(R.id.textProgressDate);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
