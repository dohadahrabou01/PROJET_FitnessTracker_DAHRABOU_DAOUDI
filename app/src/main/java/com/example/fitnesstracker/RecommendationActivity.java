package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * L'activité RecommendationActivity permet à l'utilisateur de consulter différentes recommandations
 * concernant l'hydratation, la méditation et les exercices physiques. L'utilisateur peut naviguer
 * vers des activités spécifiques pour chacune de ces catégories.

 */
public class RecommendationActivity extends AppCompatActivity {

    private Button btnHydration, btnMeditation, btnExercises;

    /**
     * Méthode appelée lors de la création de l'activité. Cette méthode initialise les boutons et
     * configure les actions à effectuer lors des clics sur ces boutons, permettant de rediriger l'utilisateur
     * vers des activités spécifiques pour consulter les recommandations correspondantes.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, permettant de restaurer les informations après un redémarrage.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // Initialisation des boutons
        btnHydration = findViewById(R.id.btn_hydration);
        btnMeditation = findViewById(R.id.btn_meditation);
        btnExercises = findViewById(R.id.btn_exercises);

        // Gestion des clics sur le bouton de recommandations d'hydratation
        btnHydration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'activité pour consulter les recommandations sur l'hydratation
                Intent intent = new Intent(RecommendationActivity.this, HydrationRecommendationActivity.class);
                startActivity(intent);
            }
        });

        // Gestion des clics sur le bouton de recommandations de méditation
        btnMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'activité pour consulter les recommandations sur la méditation
                Intent intent = new Intent(RecommendationActivity.this, MeditationRecommendationActivity.class);
                startActivity(intent);
            }
        });

        // Gestion des clics sur le bouton de recommandations d'exercices
        btnExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'activité pour consulter les recommandations sur les exercices
                Intent intent = new Intent(RecommendationActivity.this, ExercisesRecommendationActivity.class);
                startActivity(intent);
            }
        });
    }
}
