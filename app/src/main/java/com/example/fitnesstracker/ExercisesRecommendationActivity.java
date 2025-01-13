package com.example.fitnesstracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.Adapters.ExercisesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activité affichant des recommandations d'exercices à l'utilisateur dans un RecyclerView.
 * <p>
 * Cette activité charge et affiche une liste de recommandations d'exercices dans un RecyclerView.
 * Les recommandations sont sous forme de texte et sont présentées à l'utilisateur pour l'encourager à maintenir un mode de vie actif et sain.
 * </p>
 */
public class ExercisesRecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView pour afficher les recommandations d'exercices
    private ExercisesAdapter exercisesAdapter;  // Adaptateur pour lier les données au RecyclerView
    private List<String> exercisesRecommendations;  // Liste des recommandations d'exercices

    /**
     * Méthode appelée lors de la création de l'activité.
     * <p>
     * Initialisation du RecyclerView, définition de son adaptateur et ajout des recommandations d'exercices.
     * </p>
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_recommendation);

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recycler_view_exercises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Définir un LinearLayoutManager pour une liste verticale

        // Initialiser la liste des recommandations d'exercices
        exercisesRecommendations = new ArrayList<>();
        exercisesRecommendations.add("Faites 30 minutes de cardio (course, vélo) trois fois par semaine.");
        exercisesRecommendations.add("Pratiquez des exercices de renforcement musculaire (pompes, squats) au moins deux fois par semaine.");
        exercisesRecommendations.add("Incorporez des étirements après chaque séance d'exercice pour améliorer la flexibilité.");
        exercisesRecommendations.add("Essayez le yoga pour améliorer la force, l'équilibre et la flexibilité.");
        exercisesRecommendations.add("Faites des marches rapides ou des promenades en plein air pour améliorer votre endurance.");
        exercisesRecommendations.add("Incluez des exercices de respiration pour réduire le stress et améliorer votre bien-être mental.");

        // Initialiser et lier l'adaptateur avec le RecyclerView
        exercisesAdapter = new ExercisesAdapter(exercisesRecommendations);
        recyclerView.setAdapter(exercisesAdapter);
    }
}
