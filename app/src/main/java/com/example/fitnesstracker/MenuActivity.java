package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Cette activité représente le menu principal de l'application. Elle permet à l'utilisateur de naviguer vers différentes sections de l'application telles que les entraînements, les objectifs, les paramètres, les recommandations et la déconnexion.
 * Elle récupère également les informations de session de l'utilisateur pour gérer les fonctionnalités.
 *

 */
public class MenuActivity extends AppCompatActivity {

    private Button buttonTraining, buttonObjectif, buttonSettings, buttonLogout, buttonRecommendation;

    /**
     * Méthode appelée lors de la création de l'activité. Cette méthode initialise les vues et configure les actions des boutons.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, permettant de restaurer les informations après un redémarrage de l'application.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialiser les boutons à partir du layout XML
        buttonTraining = findViewById(R.id.buttonTraining);
        buttonObjectif = findViewById(R.id.buttonObjectif);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonRecommendation = findViewById(R.id.buttonRecommendation);

        // Récupérer les informations de session utilisateur
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Navigation vers différentes activités en fonction des boutons cliqués
        buttonTraining.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, TrainingListActivity.class);
            startActivity(intent);
        });

        buttonRecommendation.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, RecommendationActivity.class);
            startActivity(intent);
        });

        buttonObjectif.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, GoalsActivity.class);
            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            // Enlever les informations de session et rediriger vers la page de connexion
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.apply();

            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Empêche le retour à l'activité de menu après la déconnexion
        });
    }
}
