package com.example.fitnesstracker;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity to handle user interactions for logging and updating workout sessions.
 * It allows users to input workout details, save them to the database, or update existing records.
 * This class manages the UI for entering workout data and communicates with the database.
 */
public class TrainingActivity extends AppCompatActivity {

    private EditText durationEditText;
    private EditText distanceEditText;
    private EditText caloriesEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Spinner workoutTypeSpinner;
    private Button saveButton;

    private DatabaseHelper dbHelper;
    private int selectedWorkoutTypeId = -1; // ID of the selected workout type

    /**
     * Called when the activity is created.
     * Initializes the views, loads workout types into the spinner, and checks if an existing workout data is provided.
     * If an existing workout is provided, it loads the data for editing.
     * Also sets up the button click listener for saving or updating workout data.
     *
     * @param savedInstanceState The saved state of the activity, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // Initializing views
        durationEditText = findViewById(R.id.durationEditText);
        distanceEditText = findViewById(R.id.distanceEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        workoutTypeSpinner = findViewById(R.id.workoutTypeSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Initializing the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Load workout types into spinner
        loadWorkoutTypes();

        // Check if a workout ID is passed for editing
        int workoutId = getIntent().getIntExtra("training_id", -1);
        if (workoutId != -1) {
            loadTrainingData(workoutId); // Load existing workout data if an ID is provided
        }

        // Set click listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workoutId == -1) {
                    saveTrainingData(); // Save new workout data if no ID is provided
                } else {
                    updateTrainingData(workoutId); // Update existing workout data
                }
            }
        });
    }

    /**
     * Loads the workout types from the database and populates the spinner.
     * The spinner allows users to select the type of workout.
     */
    private void loadWorkoutTypes() {
        ArrayList<String> workoutNames = new ArrayList<>();
        final HashMap<String, Integer> workoutTypeMap = new HashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_WORKOUT_TYPES,
                new String[]{DatabaseHelper.COLUMN_WORKOUT_TYPE_ID, DatabaseHelper.COLUMN_WORKOUT_NAME},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORKOUT_TYPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORKOUT_NAME));
                workoutNames.add(name);
                workoutTypeMap.put(name, id);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Check if workout types are available
        if (workoutNames.isEmpty()) {
            Toast.makeText(this, "Aucun type d'entraînement trouvé.", Toast.LENGTH_SHORT).show();
        }

        // Update the spinner with the workout names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workoutNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(adapter);

        workoutTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = workoutNames.get(position);
                selectedWorkoutTypeId = workoutTypeMap.get(selectedName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedWorkoutTypeId = -1;
            }
        });
    }

    /**
     * Loads the training data for the selected workout ID from the database and populates the fields for editing.
     *
     * @param workoutId The ID of the workout to load.
     */
    private void loadTrainingData(int workoutId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_WORKOUTS,
                null, // Retrieve all columns
                DatabaseHelper.COLUMN_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(workoutId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            durationEditText.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DURATION))));
            distanceEditText.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISTANCE))));
            caloriesEditText.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIES))));
            startTimeEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME)));
            endTimeEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME)));

            int workoutTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORKOUT_TYPE_ID_FK));

            // Récupérez le nom correspondant à workoutTypeId
            Cursor workoutTypeCursor = db.query(
                    DatabaseHelper.TABLE_WORKOUT_TYPES,
                    new String[]{DatabaseHelper.COLUMN_WORKOUT_NAME},
                    DatabaseHelper.COLUMN_WORKOUT_TYPE_ID + " = ?",
                    new String[]{String.valueOf(workoutTypeId)},
                    null, null, null);

            if (workoutTypeCursor != null && workoutTypeCursor.moveToFirst()) {
                String workoutTypeName = workoutTypeCursor.getString(
                        workoutTypeCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WORKOUT_NAME)
                );

                // Définir la position dans le spinner
                ArrayAdapter adapter = (ArrayAdapter) workoutTypeSpinner.getAdapter();
                int spinnerPosition = adapter.getPosition(workoutTypeName);
                workoutTypeSpinner.setSelection(spinnerPosition);

                workoutTypeCursor.close();
            }

            cursor.close();
        }
    }


    /**
     * Saves the workout data entered by the user to the database.
     * If any required field is empty, a Toast message is shown.
     */
    private void saveTrainingData() {
        // Retrieve the values entered by the user
        String duration = durationEditText.getText().toString().trim();
        String distance = distanceEditText.getText().toString().trim();
        String calories = caloriesEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        // Validate fields
        if (duration.isEmpty() || distance.isEmpty() || calories.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || selectedWorkoutTypeId == -1) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the data for insertion
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DURATION, Integer.parseInt(duration));
        values.put(DatabaseHelper.COLUMN_DISTANCE, Float.parseFloat(distance));
        values.put(DatabaseHelper.COLUMN_CALORIES, Float.parseFloat(calories));
        values.put(DatabaseHelper.COLUMN_START_TIME, startTime);
        values.put(DatabaseHelper.COLUMN_END_TIME, endTime);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);
        values.put(DatabaseHelper.COLUMN_WORKOUT_TYPE_ID_FK, selectedWorkoutTypeId);

        // Insert data into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(DatabaseHelper.TABLE_WORKOUTS, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Données d'entraînement enregistrées avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TrainingListActivity.class);
            startActivity(intent);

            // Terminer l'activité actuelle
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Updates the existing workout data in the database.
     *
     * @param workoutId The ID of the workout to update.
     */
    private void updateTrainingData(int workoutId) {
        // Retrieve the values entered by the user
        String duration = durationEditText.getText().toString().trim();
        String distance = distanceEditText.getText().toString().trim();
        String calories = caloriesEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        // Validate fields
        if (duration.isEmpty() || distance.isEmpty() || calories.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || selectedWorkoutTypeId == -1) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the data for update
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DURATION, Integer.parseInt(duration));
        values.put(DatabaseHelper.COLUMN_DISTANCE, Float.parseFloat(distance));
        values.put(DatabaseHelper.COLUMN_CALORIES, Float.parseFloat(calories));
        values.put(DatabaseHelper.COLUMN_START_TIME, startTime);
        values.put(DatabaseHelper.COLUMN_END_TIME, endTime);
        values.put(DatabaseHelper.COLUMN_USER_ID_FK, userId);
        values.put(DatabaseHelper.COLUMN_WORKOUT_TYPE_ID_FK, selectedWorkoutTypeId);

        // Update the workout in the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = db.update(
                DatabaseHelper.TABLE_WORKOUTS,
                values,
                DatabaseHelper.COLUMN_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(workoutId)}
        );

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Entraînement mis à jour avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TrainingListActivity.class);
            startActivity(intent);

            // Terminer l'activité actuelle
            finish(); // Close the activity and return to the workout list
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    /**
     * Clears all the input fields and resets the spinner selection.
     */
    private void clearFields() {
        durationEditText.setText("");
        distanceEditText.setText("");
        caloriesEditText.setText("");
        startTimeEditText.setText("");
        endTimeEditText.setText("");
        workoutTypeSpinner.setSelection(0);
    }
}
