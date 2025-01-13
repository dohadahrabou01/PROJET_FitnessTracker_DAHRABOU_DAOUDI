package com.example.fitnesstracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Goal;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activité pour ajouter ou modifier un objectif.
 * Cette activité permet à l'utilisateur de définir un objectif, de le personnaliser (type, valeur cible, dates de début et de fin, statut)
 * et de l'enregistrer dans la base de données. Si un objectif existant est sélectionné, il est chargé et peut être modifié.

 */
public class GoalActivity extends AppCompatActivity {

    private EditText editTextGoalType, editTextTargetValue, editTextStartDate, editTextEndDate; // Champs de saisie pour l'objectif
    private Spinner spinnerStatus;  // Spinner pour choisir le statut de l'objectif
    private Button buttonSaveGoal;  // Bouton pour enregistrer l'objectif

    private DatabaseHelper dbHelper;  // Helper pour interagir avec la base de données
    private int goalId = -1;  // ID de l'objectif, utilisé pour la modification
    private int userId;  // ID de l'utilisateur

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les éléments de l'interface utilisateur, récupère l'ID de l'utilisateur et charge les données existantes de l'objectif si nécessaire.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        // Récupérer l'ID de l'utilisateur depuis les SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        // Initialiser les éléments de l'interface
        editTextGoalType = findViewById(R.id.editTextGoalType);
        editTextTargetValue = findViewById(R.id.editTextTargetValue);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        buttonSaveGoal = findViewById(R.id.buttonSaveGoal);
        dbHelper = new DatabaseHelper(this);

        // Récupérer l'ID de l'objectif depuis l'intent si on est en mode modification
        Intent intent = getIntent();
        goalId = intent.getIntExtra("goal_id", -1);

        // Si goalId est valide, charger les données de l'objectif existant
        if (goalId != -1) {
            loadGoalData(goalId);
        }

        // Ajouter un ArrayAdapter pour le Spinner (statut)
        String[] statuses = getResources().getStringArray(R.array.status_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Gérer la sélection dans le Spinner
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Traiter la sélection si nécessaire
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Gérer le cas où rien n'est sélectionné
            }
        });

        // Enregistrer ou mettre à jour l'objectif
        buttonSaveGoal.setOnClickListener(v -> {
            String goalType = editTextGoalType.getText().toString().trim();
            String targetValueString = editTextTargetValue.getText().toString().trim();
            String startDate = editTextStartDate.getText().toString().trim();
            String endDate = editTextEndDate.getText().toString().trim();
            String status = (String) spinnerStatus.getSelectedItem();

            // Vérification que tous les champs sont remplis
            if (TextUtils.isEmpty(goalType) || TextUtils.isEmpty(targetValueString) ||
                    TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(status)) {
                Toast.makeText(GoalActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            float targetValue = Float.parseFloat(targetValueString);

            // Si c'est un nouvel objectif, l'ajouter à la base de données
            if (goalId == -1) {
                Goal newGoal = new Goal(goalType, targetValue, startDate, endDate, status, userId);
                dbHelper.insertGoal(newGoal);
                Toast.makeText(this, "Objectif ajouté", Toast.LENGTH_SHORT).show();
            } else {
                // Si on modifie un objectif existant, mettre à jour les informations
                Goal updatedGoal = new Goal(goalId, userId, goalType, targetValue, startDate, endDate, status);
                dbHelper.updateGoal(updatedGoal);
                Toast.makeText(this, "Objectif mis à jour", Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();
        });

        // Afficher le DatePicker pour la date de début et de fin
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));
    }

    /**
     * Charge les données d'un objectif existant à partir de la base de données et les affiche dans les champs de saisie.
     *
     * @param goalId L'ID de l'objectif à charger.
     */
    private void loadGoalData(int goalId) {
        Goal goal = dbHelper.getGoal(goalId);
        String goalStatus = goal.getStatus().trim();

        if (goal != null) {
            // Remplir les champs avec les données de l'objectif
            editTextGoalType.setText(goal.getGoalType());
            editTextTargetValue.setText(String.valueOf(goal.getTargetValue()));
            editTextStartDate.setText(goal.getStartDate());
            editTextEndDate.setText(goal.getEndDate());

            // Récupérer les statuses de la ressource
            String[] statuses = getResources().getStringArray(R.array.status_array);

            // Log les statuses pour déboguer
            Log.d("Statuses", "Array of statuses: " + Arrays.toString(statuses));
            Log.d("GoalStatus", "Goal Status: '" + goalStatus + "'");

            // Vérifier si le status de l'objectif correspond à un élément du tableau
            for (int i = 0; i < statuses.length; i++) {
                Log.d("Statuses", "Comparing with status: '" + statuses[i].trim() + "'");
                if (goalStatus.equals(statuses[i].trim())) {
                    Log.d("SpinnerSelection", "Setting selection at index: " + i);
                    // Mettre à jour l'adaptateur et définir la sélection dans le spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStatus.setAdapter(adapter); // Mettre à jour l'adaptateur
                    spinnerStatus.setSelection(i); // Sélectionner l'élément correspondant
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Objectif introuvable", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Affiche un DatePickerDialog pour sélectionner une date.
     *
     * @param editText L'EditText dans lequel la date sélectionnée sera affichée.
     */
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
}
