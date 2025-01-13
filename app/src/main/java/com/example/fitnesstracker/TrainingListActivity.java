package com.example.fitnesstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.Adapters.TrainingAdapter;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Training;

import java.util.ArrayList;

/**
 * Activity to display a list of workouts for the logged-in user.
 * This activity retrieves workouts from the database based on the user's ID,
 * displays them in a RecyclerView, and allows the user to navigate to the workout form.
 */
public class TrainingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrainingAdapter trainingAdapter;
    private ArrayList<Training> trainingList;
    private DatabaseHelper dbHelper;
    private int userId;  // ID of the logged-in user
    private static final int REQUEST_CODE_PICK_FILE = 1;
    /**
     * Called when the activity is created.
     * This method sets up the RecyclerView, loads workouts from the database,
     * and sets up a button to navigate to the training form.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1); // Retrieve the stored user ID

        if (userId == -1) {
            // If the user ID is invalid, show a message and do not load the trainings
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;  // Exit the method if the user is not logged in
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        trainingList = new ArrayList<>();

        // Load the user's trainings from the database
        loadTrainings(userId);

        // Create an adapter to link the data to the RecyclerView
        trainingAdapter = new TrainingAdapter(trainingList, this);  // Pass the list and the context
        recyclerView.setAdapter(trainingAdapter);

        // Button to navigate to the training form
        Button goToFormButton = findViewById(R.id.goToFormButton);
        goToFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the intent to start the training form activity
                Intent intent = new Intent(TrainingListActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        Button exportDataButton = findViewById(R.id.exportDataButton);
        exportDataButton.setOnClickListener(view -> {
            // Lancer le sélecteur de fichiers pour choisir l'emplacement
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_TITLE, "workouts_data.csv");  // Titre du fichier
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
        });
    }
/**
 * This method is called when an activity you launched with `startActivityForResult` finishes.
 * In this case, it handles the result of the file picker, where the user selects the file location
 * for exporting workout data to a CSV file.
 *
         * @param requestCode The request code that was passed when starting the activity. This is used
 *                    to identify which activity's result is being handled.
            * @param resultCode The result of the activity. Can be `RESULT_OK` if the user has selected a file
 *                   or `RESULT_CANCELED` if the operation was canceled.
            * @param data The Intent containing the result data. It contains the URI of the file chosen by the user.
 *
         * <p>This method handles the file selection process:
            * <ul>
 *     <li>If the user successfully selected a file, it retrieves the URI of the selected file.</li>
            *     <li>Then, it uses a helper method (`DatabaseHelper.exportWorkoutsToCSV`) to export workout data
 *     into the chosen CSV file.</li>
            *     <li>It logs the file path of the exported file and shows a toast message indicating the result
 *     of the export (success or failure).</li>
            * </ul>
            * </p>
            */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
                // L'utilisateur a choisi un emplacement, obtenir le URI du fichier
                Uri uri = data.getData();

                // Exporter les données dans le fichier choisi
                DatabaseHelper dbHelper = new DatabaseHelper(this); // Passez `this` comme `Context`
                boolean isExported = dbHelper.exportWorkoutsToCSV(this, userId, uri); // Passez `this` comme `Context`

                Log.d("ExportPath", "Le fichier CSV a été exporté ici : " + uri.getPath());

                if (isExported) {
                    Toast.makeText(this, "Exportation réussie : " + uri.getPath(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Échec de l'exportation", Toast.LENGTH_SHORT).show();
                }
            }
        }


        /**
         * Loads the workouts from the database for the specified user.
         * The workouts are added to the training list and displayed in the RecyclerView.
         *
         * @param userId The ID of the user whose workouts should be loaded.
         */
    private void loadTrainings(int userId) {
        // Retrieve the data from the database for the specific user
        Cursor cursor = dbHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_WORKOUTS,
                new String[]{DatabaseHelper.COLUMN_WORKOUT_ID, DatabaseHelper.COLUMN_DURATION, DatabaseHelper.COLUMN_DISTANCE, DatabaseHelper.COLUMN_CALORIES, DatabaseHelper.COLUMN_START_TIME, DatabaseHelper.COLUMN_END_TIME, DatabaseHelper.COLUMN_WORKOUT_TYPE_ID_FK},
                DatabaseHelper.COLUMN_USER_ID + " = ?",  // Condition to filter by user ID
                new String[]{String.valueOf(userId)},  // Parameter for the user ID
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int workoutId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORKOUT_ID));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DURATION));
                float distance = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISTANCE));
                float calories = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIES));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME));

                // Add the workout to the list
                Training training = new Training(workoutId, duration, distance, calories, startTime, endTime);
                trainingList.add(training);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Toast.makeText(this, "Aucun entraînement trouvé pour cet utilisateur.", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Appelé lorsque l'activité revient à l'état de premier plan.
     * Cette méthode est utilisée pour effectuer des opérations qui doivent
     * être mises à jour chaque fois que l'utilisateur revient à l'activité.
     *
     * <p>Dans cette implémentation spécifique, les actions suivantes sont effectuées :
     * <ul>
     *     <li>Effacement de la liste actuelle des entraînements pour éviter les doublons.</li>
     *     <li>Rechargement des données d'entraînement associées à l'utilisateur connecté.</li>
     *     <li>Mise à jour de l'adaptateur pour refléter les modifications dans la liste.</li>
     * </ul>
     * </p>
     *
     * @override Cette méthode surcharge {@link android.app.Activity#onResume()}.
     * Elle appelle la méthode de la classe parent pour conserver le comportement standard
     * de l'activité.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Effacer la liste actuelle pour éviter les doublons
        trainingList.clear();

        // Recharger les données de l'utilisateur connecté
        loadTrainings(userId);

        // Notifiez l'adaptateur que les données ont changé
        if (trainingAdapter != null) {
            trainingAdapter.notifyDataSetChanged();
        }
    }

}
