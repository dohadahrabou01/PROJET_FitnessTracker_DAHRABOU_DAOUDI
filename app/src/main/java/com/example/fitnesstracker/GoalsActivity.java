package com.example.fitnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.Adapters.GoalAdapter;
import com.example.fitnesstracker.database.Goal;
import com.example.fitnesstracker.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * Activité affichant la liste des objectifs de l'utilisateur.
 * Cette activité permet à l'utilisateur de voir tous ses objectifs enregistrés dans la base de données.
 * Elle permet également de naviguer vers une autre activité pour ajouter un nouvel objectif.
 *
 * @author [Votre Nom]
 * @version 1.0
 */
public class GoalsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGoals;  // RecyclerView pour afficher les objectifs
    private GoalAdapter goalAdapter;  // Adaptateur pour lier les objectifs au RecyclerView
    private ArrayList<Goal> goalList;  // Liste des objectifs à afficher
    private DatabaseHelper dbHelper;  // Helper pour interagir avec la base de données
    private int userId;  // ID de l'utilisateur

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les éléments de l'interface, charge les objectifs depuis la base de données,
     * et configure le RecyclerView pour afficher les objectifs.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Initialiser le RecyclerView et son layout manager
        recyclerViewGoals = findViewById(R.id.recyclerViewGoals);
        recyclerViewGoals.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        goalList = new ArrayList<>();

        // Charger les objectifs depuis la base de données
        loadGoals();

        // Initialiser l'adaptateur et l'associer au RecyclerView
        goalAdapter = new GoalAdapter(this, goalList);
        recyclerViewGoals.setAdapter(goalAdapter);

        // Initialiser le bouton pour ajouter un nouvel objectif
        Button buttonAddGoal = findViewById(R.id.buttonAddGoal);
        buttonAddGoal.setOnClickListener(v -> {
            // Naviguer vers l'activité GoalActivity pour ajouter un objectif
            Intent intent = new Intent(GoalsActivity.this, GoalActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Charge la liste des objectifs de l'utilisateur depuis la base de données.
     * Récupère l'ID de l'utilisateur à partir des SharedPreferences et interroge la base de données.
     * Affiche un message d'erreur si les objectifs ne sont pas trouvés ou s'il y a une exception.
     */
    private void loadGoals() {
        try {
            // Récupérer l'ID de l'utilisateur depuis les SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            userId = sharedPreferences.getInt("user_id", -1);

            // Vérification de l'ID utilisateur
            if (userId == -1) {
                throw new Exception("User ID non trouvé. L'utilisateur n'est pas connecté.");
            }

            // Vider la liste des objectifs existants
            goalList.clear();
            // Charger les objectifs depuis la base de données
            ArrayList<Goal> goals = dbHelper.getAllGoals(userId);

            // Vérifier si des objectifs ont été récupérés
            if (goals == null || goals.isEmpty()) {
                // Aucune donnée d'objectif récupérée
                Toast.makeText(this, "Aucun objectif trouvé", Toast.LENGTH_SHORT).show();
            } else {
                // Ajouter les objectifs récupérés à la liste et mettre à jour l'adaptateur
                goalList.addAll(goals);
                goalAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            // En cas d'exception, afficher le message d'erreur
            Toast.makeText(this, "Erreur lors du chargement des objectifs: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();  // Afficher l'exception dans les logs pour le débogage
        }
    }
    /**
     * Appelé lorsque l'activité revient à l'état de premier plan.
     * Cette méthode est utilisée pour effectuer des opérations qui doivent
     * être mises à jour chaque fois que l'utilisateur revient à l'activité.
     *
     *
     *
     * @override Cette méthode surcharge {@link android.app.Activity#onResume()}.
     * Elle appelle la méthode de la classe parent pour conserver le comportement standard
     * de l'activité.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Recharger les objectifs à chaque retour à cette activité
        goalList.clear();
        loadGoals();
    }

}
