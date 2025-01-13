package com.example.fitnesstracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.database.HydrationRecommendation;

import java.util.List;

/**
 * Adapter pour afficher une liste de recommandations d'hydratation dans un RecyclerView.
 * <p>
 * Cette classe est utilisée pour lier les données de recommandations d'hydratation à un RecyclerView.
 * Chaque élément de la liste est affiché avec un texte et une icône.
 * </p>
 */
public class HydrationRecommendationAdapter extends RecyclerView.Adapter<HydrationRecommendationAdapter.ViewHolder> {

    /** Liste des recommandations d'hydratation à afficher */
    private List<HydrationRecommendation> recommendations;

    /**
     * Constructeur pour l'adaptateur.
     *
     * @param recommendations Liste des recommandations d'hydratation à afficher
     */
    public HydrationRecommendationAdapter(List<HydrationRecommendation> recommendations) {
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
                .inflate(R.layout.item_hydration_recommendation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Lie les données d'une recommandation d'hydratation à un ViewHolder.
     *
     * @param holder Le ViewHolder dans lequel les données seront liées
     * @param position La position de l'élément dans la liste
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HydrationRecommendation recommendation = recommendations.get(position);

        // Définir le texte de la recommandation
        holder.recommendationText.setText(recommendation.getRecommendation());

        // Définir l'icône appropriée pour la recommandation (par exemple, une icône d'hydratation)
        holder.icon.setImageResource(R.drawable.ic_hydratation);  // Remplacer par l'icône appropriée
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
        /** Icône représentant la recommandation */
        public ImageView icon;

        /** Texte de la recommandation */
        public TextView recommendationText;

        /**
         * Constructeur pour initialiser le ViewHolder avec les vues associées.
         *
         * @param itemView La vue représentant un élément de la liste
         */
        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            recommendationText = itemView.findViewById(R.id.recommendation_text);
        }
    }
}
