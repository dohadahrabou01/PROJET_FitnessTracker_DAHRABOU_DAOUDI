package com.example.fitnesstracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;

import java.util.List;

/**
 * Adapter pour afficher les recommandations d'exercices dans une liste à l'aide d'un RecyclerView.
 * Cette classe gère la création et l'affichage des éléments dans la liste.
 */
public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder> {

    /**
     * Liste des recommandations d'exercices à afficher.
     */
    private List<String> recommendations;

    /**
     * Constructeur de l'adaptateur.
     *
     * @param recommendations Liste des recommandations d'exercices.
     */
    public ExercisesAdapter(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * Crée une nouvelle vue pour chaque élément de la liste.
     * Cette méthode est appelée par le RecyclerView pour créer de nouvelles vues si nécessaire.
     *
     * @param parent Le parent dans lequel la nouvelle vue sera insérée.
     * @param viewType Le type de vue à créer (pas utilisé dans ce cas).
     * @return Un nouvel objet {@link ExercisesViewHolder} pour contenir la vue de l'élément.
     */
    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crée une vue à partir du fichier XML de l'élément de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_recommendation, parent, false);
        return new ExercisesViewHolder(view);
    }

    /**
     * Lie les données à la vue pour un élément spécifique de la liste.
     * Cette méthode est appelée pour chaque élément à afficher.
     *
     * @param holder Le {@link ExercisesViewHolder} qui contient la vue de l'élément.
     * @param position La position de l'élément dans la liste.
     */
    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder holder, int position) {
        // Obtient la recommandation à la position donnée et l'affiche dans le TextView
        String recommendation = recommendations.get(position);
        holder.recommendationText.setText(recommendation);
    }

    /**
     * Renvoie le nombre total d'éléments dans la liste des recommandations.
     *
     * @return Le nombre d'éléments dans la liste des recommandations.
     */
    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    /**
     * ViewHolder pour contenir la vue d'un élément d'exercice dans la liste.
     * Cette classe permet d'éviter de réinitialiser les vues à chaque fois qu'un élément est affiché.
     */
    public static class ExercisesViewHolder extends RecyclerView.ViewHolder {
        // Le TextView pour afficher la recommandation d'exercice
        TextView recommendationText;

        /**
         * Constructeur du ViewHolder.
         * Cette méthode lie les vues à leurs éléments correspondants dans le layout XML.
         *
         * @param itemView La vue de l'élément d'exercice.
         */
        public ExercisesViewHolder(View itemView) {
            super(itemView);
            // Lier le TextView de la recommandation
            recommendationText = itemView.findViewById(R.id.recommendation_text);
        }
    }
}
