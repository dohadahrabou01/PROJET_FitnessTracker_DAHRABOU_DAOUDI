package com.example.fitnesstracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.Adapters.HydrationRecommendationAdapter;
import com.example.fitnesstracker.database.HydrationRecommendation;

import java.util.ArrayList;
import java.util.List;

/**
 * Activité affichant des recommandations de consommation d'eau pour l'utilisateur.
 * Cette activité présente une liste de conseils pour aider l'utilisateur à maintenir une bonne hydratation.
 *

 */
public class HydrationRecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView pour afficher les recommandations
    private HydrationRecommendationAdapter adapter;  // Adaptateur pour lier les recommandations au RecyclerView
    private List<HydrationRecommendation> recommendationList;  // Liste des recommandations d'hydratation

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les éléments de l'interface, charge les recommandations d'hydratation et configure le RecyclerView.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydration_recommendation);

        // Initialiser le RecyclerView et son LayoutManager
        recyclerView = findViewById(R.id.recycler_view_hydration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser la liste des recommandations
        recommendationList = new ArrayList<>();
        recommendationList.add(new HydrationRecommendation("Buvez au moins 2L d'eau par jour."));
        recommendationList.add(new HydrationRecommendation("Essayez de boire un verre d'eau chaque matin."));
        recommendationList.add(new HydrationRecommendation("Évitez les boissons sucrées, préférez l'eau."));
        recommendationList.add(new HydrationRecommendation("Buvez un peu d'eau avant chaque repas."));
        recommendationList.add(new HydrationRecommendation("Assurez-vous d'avoir une bouteille d'eau à portée de main."));

        // Créer l'adaptateur et l'associer au RecyclerView
        adapter = new HydrationRecommendationAdapter(recommendationList);
        recyclerView.setAdapter(adapter);
    }
}
