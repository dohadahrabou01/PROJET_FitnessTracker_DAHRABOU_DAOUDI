package com.example.fitnesstracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;

/**
 * Activité qui permet à l'utilisateur de se connecter en fournissant son adresse email et son mot de passe.
 * Cette activité vérifie les informations de connexion dans la base de données et gère la redirection vers l'écran principal si la connexion est réussie.
 * Si l'utilisateur n'est pas encore inscrit, un lien permet de l'amener à l'écran d'inscription.

 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;  // Champs de saisie pour l'email et le mot de passe
    private Button btnLogin;  // Bouton pour se connecter
    private TextView tvRegisterLink;  // Lien vers la page d'inscription
    private DatabaseHelper dbHelper;  // Instance de la classe DatabaseHelper pour interagir avec la base de données

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les vues et définit les actions des boutons.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        // Initialisation de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Gestion du clic sur "Se connecter"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Gestion du clic sur "Créer un compte"
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirection vers la page d'inscription
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Méthode pour tenter de connecter l'utilisateur avec les informations saisies (email et mot de passe).
     * Si les informations sont valides, l'utilisateur est connecté et redirigé vers l'écran principal.
     * Sinon, un message d'erreur est affiché.
     */
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation des champs
        if (email.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre adresse email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification des informations dans la base de données
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.moveToFirst()) {
            // Connexion réussie
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)); // Récupération de l'ID utilisateur
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)); // Récupération du nom d'utilisateur

            // Sauvegarder l'ID utilisateur et le nom dans SharedPreferences
            saveUserSession(userId, username);

            Toast.makeText(this, "Bienvenue " + username + "!", Toast.LENGTH_SHORT).show();

            // Redirection vers MenuActivity
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Empêche le retour à la page de connexion
        } else {
            // Échec de la connexion
            Toast.makeText(this, "Email ou mot de passe incorrect.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    /**
     * Sauvegarde l'ID utilisateur et son nom dans les SharedPreferences pour maintenir la session active.
     *
     * @param userId L'ID de l'utilisateur connecté.
     * @param username Le nom de l'utilisateur connecté.
     */
    private void saveUserSession(int userId, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);  // Enregistrer l'ID utilisateur
        editor.putString("username", username);  // Enregistrer le nom de l'utilisateur
        editor.apply();
    }
}
