package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.Adapters.ProgressAdapter;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Progress;

import java.util.ArrayList;

/**
 * Cette activité permet à l'utilisateur de visualiser les progrès réalisés pour un objectif spécifique.
 * Elle charge les données de progrès depuis la base de données et permet à l'utilisateur d'ajouter de nouveaux progrès.
 *

 */
public class ProgressActivity extends AppCompatActivity {

    private ProgressAdapter progressAdapter;
    private ArrayList<Progress> progressList;
    private DatabaseHelper dbHelper;
    private int goalId;  // ID de l'objectif pour lequel on charge les progrès

    /**
     * Méthode appelée lors de la création de l'activité. Elle initialise les composants nécessaires,
     * charge les progrès pour un objectif spécifique et configure les boutons.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, permettant de restaurer les informations après un redémarrage.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Initialiser RecyclerView pour afficher les progrès
        RecyclerView recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        recyclerViewProgress.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la base de données et de la liste de progrès
        dbHelper = new DatabaseHelper(this);
        progressList = new ArrayList<>();

        // Récupérer l'ID de l'objectif à partir de l'intent
        goalId = getIntent().getIntExtra("goalId", -1);

        // Vérification de la validité de l'ID d'objectif
        if (goalId == -1) {
            Toast.makeText(this, "Erreur: Goal ID non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }

        // Charger les progrès associés à l'objectif
        loadProgress(goalId);

        // Initialiser l'adapter pour afficher les progrès dans le RecyclerView
        progressAdapter = new ProgressAdapter(progressList, this);
        recyclerViewProgress.setAdapter(progressAdapter);

        // Bouton pour ajouter un nouveau progrès
        Button buttonAddProgress = findViewById(R.id.buttonAddProgress);
        buttonAddProgress.setOnClickListener(v -> {
            // Intent pour ajouter un nouveau progrès
            Intent intent = new Intent(ProgressActivity.this, AddProgressActivity.class);
            intent.putExtra("goalId", goalId);  // Passer l'ID de l'objectif
            startActivity(intent);
        });
    }

    /**
     * Charge les progrès associés à un objectif spécifique depuis la base de données.
     *
     * @param goalId L'ID de l'objectif pour lequel les progrès doivent être chargés.
     */
    private void loadProgress(int goalId) {
        // Récupérer les données de la base de données pour le goalId spécifique
        Cursor cursor = dbHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_PROGRESS,  // Table des progrès
                new String[]{DatabaseHelper.COLUMN_PROGRESS_ID, DatabaseHelper.COLUMN_GOAL_ID, DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_CURRENT_VALUE},  // Colonnes à récupérer
                DatabaseHelper.COLUMN_GOAL_ID + " = ?",  // Condition pour filtrer par goalId
                new String[]{String.valueOf(goalId)},  // Paramètre du goalId
                null, null, null  // Pas de groupBy, having ou orderBy
        );

        // Vérifier si le curseur contient des données
        if (cursor != null && cursor.moveToFirst()) {
            progressList.clear();  // Vider la liste actuelle des progrès

            do {
                // Extraire les données du curseur
                int progressId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROGRESS_ID));
                int goalIdFromDB = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOAL_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                double value = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CURRENT_VALUE));

                // Créer un objet Progress et l'ajouter à la liste
                Progress progress = new Progress(progressId, goalIdFromDB, value, date);
                progressList.add(progress);
            } while (cursor.moveToNext());  // Passer à l'entrée suivante

            cursor.close();  // Fermer le curseur
        } else {
            // Aucun progrès trouvé pour ce goalId
            Toast.makeText(this, "Aucun progrès trouvé pour cet objectif.", Toast.LENGTH_SHORT).show();
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
        progressList.clear();
        loadProgress(goalId);
        progressAdapter.notifyDataSetChanged();// Recharger les progrès à chaque reprise de l'activité
    }

}
