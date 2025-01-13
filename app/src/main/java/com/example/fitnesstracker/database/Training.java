package com.example.fitnesstracker.database;

/**
 * Représente une séance d'entraînement ou un exercice physique effectué par un utilisateur.
 * Cette classe contient des informations sur la durée de l'entraînement, la distance parcourue, les calories brûlées,
 * ainsi que l'heure de début et de fin de l'entraînement.
 */
public class Training {
    private int workoutId;
    private int duration;
    private float distance;
    private float calories;
    private String startTime;
    private String endTime;

    /**
     * Constructeur pour initialiser un enregistrement de séance d'entraînement.
     *
     * @param workoutId L'ID unique de l'entraînement.
     * @param duration La durée de l'entraînement en minutes.
     * @param distance La distance parcourue pendant l'entraînement, en kilomètres ou en mètres.
     * @param calories Le nombre de calories brûlées pendant l'entraînement.
     * @param startTime L'heure de début de l'entraînement (au format "HH:mm").
     * @param endTime L'heure de fin de l'entraînement (au format "HH:mm").
     */
    public Training(int workoutId, int duration, float distance, float calories, String startTime, String endTime) {
        this.workoutId = workoutId;
        this.duration = duration;
        this.distance = distance;
        this.calories = calories;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Récupère l'ID de l'entraînement.
     *
     * @return L'ID unique de l'entraînement.
     */
    public int getWorkoutId() {
        return workoutId;
    }

    /**
     * Récupère la durée de l'entraînement.
     *
     * @return La durée de l'entraînement en minutes.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Récupère la distance parcourue pendant l'entraînement.
     *
     * @return La distance parcourue pendant l'entraînement, en kilomètres ou en mètres.
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Récupère le nombre de calories brûlées pendant l'entraînement.
     *
     * @return Le nombre de calories brûlées pendant l'entraînement.
     */
    public float getCalories() {
        return calories;
    }

    /**
     * Récupère l'heure de début de l'entraînement.
     *
     * @return L'heure de début de l'entraînement (au format "HH:mm").
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Récupère l'heure de fin de l'entraînement.
     *
     * @return L'heure de fin de l'entraînement (au format "HH:mm").
     */
    public String getEndTime() {
        return endTime;
    }
}
