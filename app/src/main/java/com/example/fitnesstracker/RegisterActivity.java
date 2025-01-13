package com.example.fitnesstracker;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;

/**
 * L'activité RegisterActivity permet à l'utilisateur de s'inscrire en créant un compte
 * avec des informations personnelles telles que le nom d'utilisateur, l'email, le mot de passe,
 * l'âge, le poids, la taille et le genre. Une fois l'inscription réussie, l'utilisateur est
 * redirigé vers la page de connexion.

 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etAge, etWeight, etHeight;
    private Spinner spinnerGender;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    private TextView tvLoginLink;

    /**
     * Méthode appelée lors de la création de l'activité. Cette méthode initialise les champs de saisie
     * pour l'inscription, remplit le Spinner pour le genre et configure l'action lors du clic sur le
     * bouton d'enregistrement et le lien pour se connecter.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, permettant de restaurer les informations après un redémarrage.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser les champs
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DatabaseHelper(this);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // Remplir le Spinner pour le genre (exemple simple avec Male et Female)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Gérer le clic sur le bouton d'enregistrement
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String ageString = etAge.getText().toString().trim();
            String weightString = etWeight.getText().toString().trim();
            String heightString = etHeight.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();

            // Vérifier si tous les champs sont remplis
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(ageString) ||
                    TextUtils.isEmpty(weightString) || TextUtils.isEmpty(heightString)) {
                Toast.makeText(RegisterActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageString);
            float weight = Float.parseFloat(weightString);
            float height = Float.parseFloat(heightString);

            // Insérer l'utilisateur dans la base de données
            boolean isInserted = dbHelper.insertUser(username, email, password, age, gender, weight, height);

            // Vérifier si l'insertion a réussi
            if (isInserted) {
                Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                // Enregistrer la session de l'utilisateur
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("email", email);
                editor.apply();
                // Rediriger vers l'écran de connexion
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Fermer l'activité d'inscription
            } else {
                Toast.makeText(RegisterActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
            }
        });

        // Lien pour rediriger l'utilisateur vers la page de connexion
        tvLoginLink.setOnClickListener(v -> {
            // Rediriger vers la page de connexion
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
