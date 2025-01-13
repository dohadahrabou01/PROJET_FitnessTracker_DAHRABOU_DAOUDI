package com.example.fitnesstracker;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.User;

/**
 * L'activité SettingsActivity permet à l'utilisateur de modifier ses paramètres personnels,
 * y compris son âge, son poids, sa taille et son genre. Elle permet également de calculer et
 * d'afficher des recommandations sur la gestion du poids en fonction de l'IMC de l'utilisateur.

 */
public class SettingsActivity extends AppCompatActivity {

    private EditText etAge, etWeight, etHeight;
    private Spinner spinnerGender;
    private Button btnSaveChanges, btnEdit;
    private TextView tvRecommendation;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private String currentUsername;
    private User currentUser;
    private View formEdit;
    private int userId;

    /**
     * Méthode appelée lors de la création de l'activité. Cette méthode initialise les vues,
     * charge les informations de l'utilisateur, et configure les actions sur les boutons Modifier
     * et Sauvegarder les changements.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, permettant de restaurer les informations après un redémarrage.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialiser les champs
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnEdit = findViewById(R.id.btnEdit);
        tvRecommendation = findViewById(R.id.tvRecommendation);
        formEdit = findViewById(R.id.formEdit);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        userId = sharedPreferences.getInt("user_id", -1);

        // Remplir le Spinner pour le genre
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Charger les informations actuelles de l'utilisateur
        loadUserData();

        // Gérer le clic sur le bouton Modifier
        btnEdit.setOnClickListener(v -> {
            formEdit.setVisibility(View.VISIBLE); // Afficher le formulaire de modification
            btnEdit.setVisibility(View.GONE); // Cacher le bouton Modifier
        });

        // Gérer le clic sur le bouton Sauvegarder les modifications
        btnSaveChanges.setOnClickListener(v -> {
            String ageString = etAge.getText().toString().trim();
            String weightString = etWeight.getText().toString().trim();
            String heightString = etHeight.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();

            if (TextUtils.isEmpty(ageString) || TextUtils.isEmpty(weightString) || TextUtils.isEmpty(heightString)) {
                Toast.makeText(SettingsActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageString);
            float weight = Float.parseFloat(weightString);
            float height = Float.parseFloat(heightString);

            // Mettre à jour l'utilisateur dans la base de données
            boolean isUpdated = dbHelper.updateUser(userId, age, gender, weight, height);

            if (isUpdated) {
                // Recharger les données après mise à jour
                loadUserData();
                formEdit.setVisibility(View.GONE); // Cacher le formulaire de modification
                btnEdit.setVisibility(View.VISIBLE); // Afficher le bouton Modifier
                Toast.makeText(SettingsActivity.this, "Paramètres mis à jour", Toast.LENGTH_SHORT).show();

                // Calculer et afficher la recommandation sur le poids
                calculateAndDisplayRecommendation(weight, height);
            } else {
                Toast.makeText(SettingsActivity.this, "Erreur lors de la mise à jour des paramètres", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Charge les informations de l'utilisateur actuel depuis la base de données et pré-remplit
     * les champs de l'interface utilisateur avec ces données.
     */
    private void loadUserData() {
        // Charger les données de l'utilisateur actuel depuis la base de données
        currentUser = dbHelper.getUserById(userId);
        if (currentUser != null) {
            // Pré-remplir les champs avec les données de l'utilisateur
            etAge.setText(String.valueOf(currentUser.getAge()));
            etWeight.setText(String.valueOf(currentUser.getWeight()));
            etHeight.setText(String.valueOf(currentUser.getHeight()));

            // Mettre à jour d'autres parties de l'interface avec les données
            TextView tvAge = findViewById(R.id.tvAge);
            tvAge.setText(String.valueOf(currentUser.getAge()));

            TextView tvWeight = findViewById(R.id.tvWeight);
            tvWeight.setText(String.valueOf(currentUser.getWeight()));

            TextView tvHeight = findViewById(R.id.tvHeight);
            tvHeight.setText(String.valueOf(currentUser.getHeight()));
            TextView tvGender = findViewById(R.id.tvGender);
            tvGender.setText(String.valueOf(currentUser.getGender()));
            // Sélectionner le genre correct
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerGender.getAdapter();
            int genderPosition = adapter.getPosition(currentUser.getGender());
            spinnerGender.setSelection(genderPosition);

            // Afficher la recommandation sur le poids
            calculateAndDisplayRecommendation(currentUser.getWeight(), currentUser.getHeight());
        }
    }

    /**
     * Calcule l'Indice de Masse Corporelle (IMC) en fonction du poids et de la taille,
     * puis génère une recommandation basée sur l'IMC.
     *
     * @param weight Le poids de l'utilisateur en kilogrammes.
     * @param height La taille de l'utilisateur en centimètres.
     */
    private void calculateAndDisplayRecommendation(float weight, float height) {
        // Convertir la hauteur en mètres si nécessaire (exemple: 175 cm = 1.75m)
        float heightInMeters = height / 100;

        // Calculer l'IMC
        float imc = weight / (heightInMeters * heightInMeters);

        // Déterminer la recommandation en fonction de l'IMC
        String recommendation;
        if (imc < 18.5) {
            recommendation = "Vous devez augmenter votre poids (IMC inférieur à 18.5).";
        } else if (imc >= 18.5 && imc < 24.9) {
            recommendation = "Votre poids est dans la plage normale (IMC entre 18.5 et 24.9).";
        } else {
            recommendation = "Vous devez réduire votre poids (IMC supérieur à 24.9).";
        }

        // Afficher la recommandation dans le TextView
        tvRecommendation.setText(recommendation);
    }
}
