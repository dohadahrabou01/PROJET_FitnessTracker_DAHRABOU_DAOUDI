package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.database.Progress;

/**
 * Activité permettant d'ajouter ou de modifier un progrès lié à un objectif.
 * <p>
 * Cette activité permet à l'utilisateur d'entrer la valeur du progrès (par exemple, distance parcourue, calories brûlées),
 * de sélectionner une date et de sauvegarder les données dans la base de données. Si un progrès existe déjà, l'utilisateur
 * peut également modifier ce progrès.
 * </p>
 *
 * L'ID de l'objectif est passé à l'activité via l'Intent. Si un ID de progrès est également passé,
 * cela signifie que l'utilisateur modifie un progrès existant.
 */
public class AddProgressActivity extends AppCompatActivity {

    private EditText editTextProgressValue;
    private DatePicker datePickerProgress;  // DatePicker pour sélectionner la date du progrès
    private Button buttonSaveProgress;
    private DatabaseHelper dbHelper;
    private int goalId;  // ID de l'objectif auquel le progrès est associé
    private int progressId = -1;  // ID du progrès à modifier, initialement -1 pour ajouter un nouveau progrès

    /**
     * Méthode appelée lors de la création de l'activité.
     * <p>
     * Récupère les informations nécessaires depuis l'Intent et configure les éléments de l'interface utilisateur.
     * Permet également de gérer la modification d'un progrès existant si l'ID du progrès est passé dans l'Intent.
     * </p>
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);

        dbHelper = new DatabaseHelper(this);

        // Récupérer les ID d'objectif et de progrès depuis l'Intent
        goalId = getIntent().getIntExtra("goalId", -1);
        progressId = getIntent().getIntExtra("progress_id", -1);

        if (goalId == -1) {
            Toast.makeText(this, "Erreur: Goal ID non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialisation des éléments de l'interface utilisateur
        editTextProgressValue = findViewById(R.id.editTextProgressValue);
        datePickerProgress = findViewById(R.id.datePickerProgress);
        buttonSaveProgress = findViewById(R.id.buttonSaveProgress);

        // Si progressId est valide, on est en mode modification
        if (progressId != -1) {
            // Récupérer et afficher les données du progrès existant
            Progress progress = dbHelper.getProgressById(progressId);
            if (progress != null) {
                editTextProgressValue.setText(String.valueOf(progress.getCurrentValue()));
                // Mettre à jour le DatePicker avec la date du progrès existant
                String[] dateParts = progress.getDate().split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int day = Integer.parseInt(dateParts[2]);
                datePickerProgress.updateDate(year, month, day);
            } else {
                Toast.makeText(this, "Erreur: Progrès non trouvé", Toast.LENGTH_SHORT).show();
            }
        }

        // Sauvegarder le progrès (ajouter ou modifier)
        buttonSaveProgress.setOnClickListener(v -> {
            // Validation des entrées
            String progressValueString = editTextProgressValue.getText().toString();

            if (progressValueString.isEmpty()) {
                Toast.makeText(AddProgressActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                double progressValue = Double.parseDouble(progressValueString);

                // Récupérer la date sélectionnée dans le DatePicker
                int day = datePickerProgress.getDayOfMonth();
                int month = datePickerProgress.getMonth() + 1;
                int year = datePickerProgress.getYear();
                String progressDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);

                // Si l'ID du progrès est valide, mettre à jour
                if (progressId != -1) {
                    boolean success = dbHelper.updateProgress(progressId, goalId, progressValue, progressDate);

                    if (success) {
                        Toast.makeText(AddProgressActivity.this, "Progrès modifié avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddProgressActivity.this, "Erreur lors de la modification du progrès", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Ajouter un nouveau progrès
                    boolean success = dbHelper.addProgress(goalId, progressValue, progressDate);

                    if (success) {
                        Toast.makeText(AddProgressActivity.this, "Progrès ajouté avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddProgressActivity.this, "Erreur lors de l'ajout du progrès", Toast.LENGTH_SHORT).show();
                    }
                }

                // Retourner à l'écran des progrès après ajout ou modification
                Intent intent = new Intent(AddProgressActivity.this, ProgressActivity.class);
                intent.putExtra("goalId", goalId);
                startActivity(intent);
                finish(); // Fermer l'activité actuelle
            }
        });
    }
}
