package com.example.fitnesstracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;

import java.util.List;

/**
 * Adapter pour afficher une liste de recommandations de méditation dans un RecyclerView.
 * <p>
 * Cette classe est utilisée pour lier une liste de chaînes de texte représentant des recommandations de méditation à un RecyclerView.
 * Chaque élément de la liste est affiché avec un texte et une icône.
 * </p>
 */
public class MeditationRecommendationAdapter extends RecyclerView.Adapter<MeditationRecommendationAdapter.ViewHolder> {

    /** Liste des recommandations de méditation à afficher */
    private List<String> recommendations;

    /**
     * Constructeur pour l'adaptateur.
     *
     * @param recommendations Liste des recommandations de méditation à afficher
     */
    public MeditationRecommendationAdapter(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * Crée et retourne un ViewHolder pour un item de la liste.
     *
     * @param parent Vue parent dans laquelle l'élément sera inséré
     * @param viewType Type de vue (non utilisé dans ce cas)
     * @return Un ViewHolder pour l'élément de la liste
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Gonfler le layout personnalisé pour chaque élément de la liste
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meditation_recommendation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Lie les données d'une recommandation de méditation à un ViewHolder.
     *
     * @param holder Le ViewHolder dans lequel les données seront liées
     * @param position La position de l'élément dans la liste
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String recommendation = recommendations.get(position);

        // Définir le texte de la recommandation
        holder.recommendationText.setText(recommendation);
    }

    /**
     * Retourne le nombre d'éléments dans la liste des recommandations.
     *
     * @return Le nombre d'éléments dans la liste des recommandations
     */
    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    /**
     * ViewHolder qui représente un élément de la liste de recommandations.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /** Texte de la recommandation */
        TextView recommendationText;

        /** Icône associée à la recommandation */
        ImageView icon;

        /**
         * Constructeur pour initialiser le ViewHolder avec les vues associées.
         *
         * @param itemView La vue représentant un élément de la liste
         */
        public ViewHolder(View itemView) {
            super(itemView);
            recommendationText = itemView.findViewById(R.id.recommendation_text);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
