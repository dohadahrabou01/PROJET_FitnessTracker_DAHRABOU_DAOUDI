package com.example.fitnesstracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe qui gère l'accès à la base de données SQLite pour l'application FitnessTracker.
 * Elle permet de créer et de gérer les tables liées aux utilisateurs, types d'entraînements,
 * entraînements effectués, objectifs et progrès.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Nom de la base de données et version
    private static final String DATABASE_NAME = "fitness_app2.db";
    private static final int DATABASE_VERSION = 1;

    // Tables et colonnes
    // Table Users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Table Workout Types
    public static final String TABLE_WORKOUT_TYPES = "workout_types";
    public static final String COLUMN_WORKOUT_TYPE_ID = "workout_type_id";
    public static final String COLUMN_WORKOUT_NAME = "name";
    public static final String COLUMN_WORKOUT_DESCRIPTION = "description";

    // Table Workouts
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_WORKOUT_ID = "workout_id";
    public static final String COLUMN_USER_ID_FK = "user_id";
    public static final String COLUMN_WORKOUT_TYPE_ID_FK = "workout_type_id";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";

    // Table Goals
    public static final String TABLE_GOALS = "goals";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_GOAL_TYPE = "goal_type";
    public static final String COLUMN_TARGET_VALUE = "target_value";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_STATUS = "status";

    // Table Progress
    public static final String TABLE_PROGRESS = "progress";
    public static final String COLUMN_PROGRESS_ID = "progress_id";
    public static final String COLUMN_GOAL_ID_FK = "goal_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CURRENT_VALUE = "current_value";


    // Commandes de création des tables
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_WEIGHT + " REAL, " +
                    COLUMN_HEIGHT + " REAL, " +
                    COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

    private static final String CREATE_TABLE_WORKOUT_TYPES =
            "CREATE TABLE " + TABLE_WORKOUT_TYPES + " (" +
                    COLUMN_WORKOUT_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORKOUT_NAME + " TEXT NOT NULL, " +
                    COLUMN_WORKOUT_DESCRIPTION + " TEXT" +
                    ");";

    private static final String CREATE_TABLE_WORKOUTS =
            "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                    COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_WORKOUT_TYPE_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_DURATION + " INTEGER, " +
                    COLUMN_DISTANCE + " REAL, " +
                    COLUMN_CALORIES + " REAL, " +
                    COLUMN_START_TIME + " TIMESTAMP, " +
                    COLUMN_END_TIME + " TIMESTAMP, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_WORKOUT_TYPE_ID_FK + ") REFERENCES " + TABLE_WORKOUT_TYPES + "(" + COLUMN_WORKOUT_TYPE_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_GOALS =
            "CREATE TABLE " + TABLE_GOALS + " (" +
                    COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_GOAL_TYPE + " TEXT NOT NULL, " +
                    COLUMN_TARGET_VALUE + " REAL NOT NULL, " +
                    COLUMN_START_DATE + " DATE, " +
                    COLUMN_END_DATE + " DATE, " +
                    COLUMN_STATUS + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_PROGRESS =
            "CREATE TABLE " + TABLE_PROGRESS + " (" +
                    COLUMN_PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_GOAL_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_DATE + " DATE NOT NULL, " +
                    COLUMN_CURRENT_VALUE + " REAL, " +
                    "FOREIGN KEY (" + COLUMN_GOAL_ID_FK + ") REFERENCES " + TABLE_GOALS + "(" + COLUMN_GOAL_ID + ")" +
                    ");";

    /**
     * Constructeur pour créer une instance de DatabaseHelper avec le contexte de l'application.
     *
     * @param context Le contexte de l'application.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * Crée toutes les tables dans la base de données à l'initialisation.
     *
     * @param db La base de données SQLite.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création des tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_WORKOUT_TYPES);
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_GOALS);
        db.execSQL(CREATE_TABLE_PROGRESS);


        // Insertion des types d'entraînements par défaut
        db.execSQL("INSERT INTO " + TABLE_WORKOUT_TYPES + " (" + COLUMN_WORKOUT_NAME + ", " + COLUMN_WORKOUT_DESCRIPTION + ") " +
                "VALUES ('Course', 'Entraînement de course à pied');");
        db.execSQL("INSERT INTO " + TABLE_WORKOUT_TYPES + " (" + COLUMN_WORKOUT_NAME + ", " + COLUMN_WORKOUT_DESCRIPTION + ") " +
                "VALUES ('Cyclisme', 'Entraînement de vélo');");
        db.execSQL("INSERT INTO " + TABLE_WORKOUT_TYPES + " (" + COLUMN_WORKOUT_NAME + ", " + COLUMN_WORKOUT_DESCRIPTION + ") " +
                "VALUES ('Natation', 'Entraînement de natation');");

     }
    /**
     * Mise à jour de la base de données lorsque la version change. Cela supprime les anciennes tables et crée de nouvelles tables.
     *
     * @param db         La base de données SQLite.
     * @param oldVersion La version précédente de la base de données.
     * @param newVersion La nouvelle version de la base de données.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppression des tables si elles existent
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);

        // Recréer les tables
        onCreate(db);
    }
    /**
     * Récupère un objet `Progress` à partir de son identifiant.
     *
     * @param progressId L'identifiant du progrès.
     * @return L'objet Progress ou null si non trouvé.
     */
    public Progress getProgressById(int progressId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROGRESS, null, COLUMN_PROGRESS_ID + " = ?", new String[]{String.valueOf(progressId)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range") Progress progress = new Progress(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PROGRESS_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_GOAL_ID)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_CURRENT_VALUE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
            );
            cursor.close();
            return progress;
        }

        return null;
    }
    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param userId   L'identifiant de l'utilisateur.
     * @param age      L'âge de l'utilisateur.
     * @param gender   Le genre de l'utilisateur.
     * @param weight   Le poids de l'utilisateur.
     * @param height   La taille de l'utilisateur.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean updateUser(int userId, int age, String gender, float weight, float height) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIGHT, height);

        // Mise à jour de l'utilisateur en utilisant l'ID de l'utilisateur
        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsUpdated > 0;
    }
    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return L'objet User ou null si non trouvé.
     */
    @SuppressLint("Range")
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            // Requête pour récupérer l'utilisateur par ID
            cursor = db.query(TABLE_USERS, // Nom de la table
                    new String[]{ COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_AGE, COLUMN_GENDER, COLUMN_WEIGHT, COLUMN_HEIGHT}, // Colonnes
                    COLUMN_USER_ID + " = ?", // Condition de sélection
                    new String[]{String.valueOf(userId)}, // Valeur de l'ID
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Créer un objet User avec les données récupérées
                 int id = cursor.getInt(cursor.getColumnIndex( COLUMN_USER_ID));
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                float weight = cursor.getFloat(cursor.getColumnIndex(COLUMN_WEIGHT));
                float height = cursor.getFloat(cursor.getColumnIndex(COLUMN_HEIGHT));

                user = new User(id, username, age, gender, weight, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();
        return user;
    }


    /**
     * Supprime un entraînement de la base de données en utilisant son identifiant.
     *
     * @param workoutId L'identifiant de l'entraînement à supprimer.
     * @return Le nombre de lignes supprimées.
     */

    public int deleteTraining(int workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WORKOUTS, COLUMN_WORKOUT_ID + " = ?", new String[]{String.valueOf(workoutId)});
    }
    /**
     * Insère un nouvel objectif dans la base de données.
     *
     * @param goal L'objet Goal à insérer.
     * @return L'identifiant de l'objectif inséré ou -1 en cas d'échec.
     */
    public long insertGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, goal.getUserId());
        values.put(COLUMN_GOAL_TYPE, goal.getGoalType());
        values.put(COLUMN_TARGET_VALUE, goal.getTargetValue());
        values.put(COLUMN_START_DATE, goal.getStartDate());
        values.put(COLUMN_END_DATE, goal.getEndDate());
        values.put(COLUMN_STATUS, goal.getStatus());

        return db.insert(TABLE_GOALS, null, values);
    }
    /**
     * Récupère tous les objectifs d'un utilisateur donné.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Une liste d'objets Goal correspondant à l'utilisateur.
     */
    @SuppressLint("Range")
    public ArrayList<Goal> getAllGoals(int userId) {
        ArrayList<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GOALS, null, COLUMN_USER_ID_FK + " = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_GOAL_ID)));
                goal.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_FK)));
                goal.setGoalType(cursor.getString(cursor.getColumnIndex(COLUMN_GOAL_TYPE)));
                goal.setTargetValue(cursor.getFloat(cursor.getColumnIndex(COLUMN_TARGET_VALUE)));
                goal.setStartDate(cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE)));
                goal.setEndDate(cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE)));
                goal.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                goals.add(goal);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return goals;
    }
    /**
     * Récupère un objectif spécifique par son identifiant.
     *
     * @param goalId L'identifiant de l'objectif.
     * @return L'objet Goal ou null si non trouvé.
     */
    public Goal getGoal(int goalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Goal goal = null;

        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE " + COLUMN_GOAL_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(goalId)});

        if (cursor != null && cursor.moveToFirst()) {
            String goalType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GOAL_TYPE));
            float targetValue = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_TARGET_VALUE));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));

            goal = new Goal( goalType, targetValue, startDate, endDate, status);
            cursor.close();
        }

        return goal;
    }
    /**
     * Met à jour un objectif dans la base de données.
     *
     * @param goal L'objet Goal à mettre à jour.
     * @return Le nombre de lignes mises à jour.
     */
    public int updateGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_TYPE, goal.getGoalType());
        values.put(COLUMN_TARGET_VALUE, goal.getTargetValue());
        values.put(COLUMN_START_DATE, goal.getStartDate());
        values.put(COLUMN_END_DATE, goal.getEndDate());
        values.put(COLUMN_STATUS, goal.getStatus());

        return db.update(TABLE_GOALS, values, COLUMN_GOAL_ID + " = ?", new String[]{String.valueOf(goal.getGoalId())});
    }
    /**
     * Supprime un objectif de la base de données en utilisant son identifiant.
     *
     * @param goalId L'identifiant de l'objectif à supprimer.
     * @return Le nombre de lignes supprimées.
     */
    public int deleteGoal(int goalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GOALS, COLUMN_GOAL_ID + " = ?", new String[]{String.valueOf(goalId)});
    }
    /**
     * Insère un nouvel utilisateur dans la base de données.
     *
     * @param username Le nom d'utilisateur.
     * @param email    L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @param age      L'âge de l'utilisateur.
     * @param gender   Le genre de l'utilisateur.
     * @param weight   Le poids de l'utilisateur.
     * @param height   La taille de l'utilisateur.
     * @return true si l'insertion a réussi, false sinon.
     */

    public boolean insertUser(String username, String email, String password, int age, String gender, float weight, float height) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("age", age);
        contentValues.put("gender", gender);
        contentValues.put("weight", weight);
        contentValues.put("height", height);

        long result = db.insert("users", null, contentValues);

        return result != -1; // Si l'insertion a échoué, retourner false
    }



    /**
     * Met à jour un enregistrement de progrès dans la base de données.
     *
     * Cette méthode met à jour un enregistrement de progrès en fonction de son identifiant
     * (`progressId`). Elle met à jour les valeurs associées à l'objectif et à la date
     * du progrès. Si la mise à jour réussit, elle renvoie `true`, sinon `false`.
     *
     * @param progressId L'identifiant du progrès à mettre à jour.
     * @param goalId L'identifiant de l'objectif associé au progrès.
     * @param progressValue La nouvelle valeur du progrès.
     * @param progressDate La nouvelle date du progrès.
     * @return `true` si la mise à jour a réussi (au moins une ligne a été modifiée),
     *         sinon `false`.
     */

    public boolean updateProgress(int progressId, int goalId, double progressValue, String progressDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_ID, goalId);  // Mettre à jour le goalId
        values.put(COLUMN_CURRENT_VALUE, progressValue);
        values.put(COLUMN_DATE, progressDate);

        // Mettre à jour le progrès avec l'ID donné
        int rowsUpdated = db.update(TABLE_PROGRESS, values, COLUMN_PROGRESS_ID + " = ?", new String[]{String.valueOf(progressId)});

        db.close();

        return rowsUpdated > 0;  // Retourne true si au moins une ligne a été mise à jour
    }
    /**
     * Supprime un enregistrement de progrès de la base de données.
     *
     * Cette méthode supprime un enregistrement de progrès en fonction de son identifiant
     * (`progressId`). Si l'opération réussit, elle renvoie le nombre de lignes supprimées.
     *
     * @param progressId L'identifiant du progrès à supprimer.
     * @return Le nombre de lignes supprimées (0 si aucune ligne n'a été supprimée).
     */

    public int deleteProgress(int progressId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Suppression de l'enregistrement avec l'ID correspondant
        return db.delete(TABLE_PROGRESS, COLUMN_PROGRESS_ID + " = ?", new String[]{String.valueOf(progressId)});
    }
    /**
     * Ajoute un nouvel enregistrement de progrès dans la base de données.
     *
     * Cette méthode insère un nouvel enregistrement de progrès pour un objectif donné.
     * Si l'insertion réussit, elle renvoie `true`, sinon `false`.
     *
     * @param goalId L'identifiant de l'objectif pour lequel le progrès est enregistré.
     * @param progressValue La valeur du progrès.
     * @param progressDate La date du progrès.
     * @return `true` si l'insertion a réussi, sinon `false`.
     */
    public boolean addProgress(int goalId, double progressValue, String progressDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_ID_FK, goalId);
        values.put(COLUMN_CURRENT_VALUE, progressValue);
        values.put(COLUMN_DATE, progressDate);

        long result = db.insert(TABLE_PROGRESS, null, values);
        db.close();

        return result != -1;
    }
    /**
     * Exporte les données des entraînements d'un utilisateur spécifié dans un fichier CSV.
     * La méthode écrit les informations des entraînements dans un fichier à l'emplacement spécifié par l'URI.
     *
     * @param context Le contexte de l'application, utilisé pour obtenir un `ContentResolver` afin d'ouvrir un flux de sortie vers l'URI.
     * @param userId L'identifiant de l'utilisateur dont les entraînements doivent être exportés.
     * @param uri L'URI du fichier où les données doivent être enregistrées.
     *
     * @return `true` si l'exportation a réussi, sinon `false`.
     *
     * <p>Cette méthode effectue les actions suivantes :</p>
     * <ul>
     *     <li>Elle ouvre un flux de sortie vers l'URI spécifié pour écrire les données dans le fichier.</li>
     *     <li>Elle écrit un en-tête CSV contenant les colonnes "Duration, Distance, Calories, StartTime, EndTime".</li>
     *     <li>Elle récupère les données des entraînements de la base de données pour l'utilisateur spécifié.</li>
     *     <li>Elle écrit les lignes correspondantes aux données d'entraînement dans le fichier CSV.</li>
     *     <li>Elle ferme les ressources (curseur et flux de sortie) après l'écriture des données.</li>
     * </ul>
     *
     * <p>En cas d'échec de l'exportation (erreur d'entrée/sortie), la méthode renvoie `false` et logue l'erreur.</p>
     */
    public boolean exportWorkoutsToCSV(Context context,int userId, Uri uri) {
        try {
            // Utilisez un OutputStream pour écrire dans le fichier URI
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);

            if (outputStream != null) {
                // Écrire les données CSV dans le fichier
                String header = "Duration,Distance,Calories,StartTime,EndTime\n";
                outputStream.write(header.getBytes());

                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_USER_ID_FK + " = ?",
                        new String[]{String.valueOf(userId)});

                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String row =  cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION)) + "," +
                            cursor.getDouble(cursor.getColumnIndex(COLUMN_DISTANCE)) + "," +
                            cursor.getDouble(cursor.getColumnIndex(COLUMN_CALORIES)) + "," +
                            cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME)) + "," +
                            cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME)) + "\n";
                    outputStream.write(row.getBytes());
                }
                cursor.close();
                outputStream.close();
                return true;
            }
        } catch (IOException e) {
            Log.e("ExportError", "Erreur lors de l'exportation : " + e.getMessage());
        }
        return false;
    }







}
