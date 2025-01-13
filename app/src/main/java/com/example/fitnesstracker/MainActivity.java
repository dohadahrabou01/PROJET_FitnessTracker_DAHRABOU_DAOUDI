package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnesstracker.database.DatabaseHelper;

/**
 * Activité principale de l'application qui permet à l'utilisateur de choisir entre se connecter
 * ou s'inscrire. Elle est également responsable de la création de la base de données nécessaire
 * pour stocker les informations des utilisateurs.
 *

 */
public class MainActivity extends AppCompatActivity {

    private Button btnLogin;  // Bouton pour accéder à la page de connexion
    private Button btnRegister;  // Bouton pour accéder à la page d'inscription
    private DatabaseHelper dbHelper;  // Instance de la classe DatabaseHelper pour gérer la base de données

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les composants de l'interface utilisateur et configure les actions des boutons.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des composants
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Création de la base de données
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.getWritableDatabase();
            Toast.makeText(this, "Base de données créée avec succès !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la création de la base de données.", Toast.LENGTH_SHORT).show();
        }

        // Gestion des clics sur "Se connecter"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Gestion des clics sur "Créer un compte"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers RegisterActivity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
