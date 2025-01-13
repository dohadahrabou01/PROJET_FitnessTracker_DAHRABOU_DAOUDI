package com.example.fitnesstracker.database;

/**
 * Représente un enregistrement de progrès lié à un objectif spécifique.
 * Cette classe contient des informations sur l'ID du progrès, l'ID de l'objectif, la valeur actuelle du progrès et la date de l'enregistrement.
 */
public class Progress {
    private int id;
    private int goalId;
    private double currentValue;
    private String date;

    /**
     * Constructeur avec paramètres pour créer un enregistrement de progrès.
     *
     * @param id L'ID unique du progrès.
     * @param goalId L'ID de l'objectif auquel ce progrès est lié.
     * @param currentValue La valeur actuelle du progrès (par exemple, la distance parcourue ou les calories brûlées).
     * @param date La date à laquelle le progrès a été enregistré (par exemple, "2025-01-12").
     */
    public Progress(int id, int goalId, double currentValue, String date) {
        this.id = id;
        this.goalId = goalId;
        this.currentValue = currentValue;
        this.date = date;
    }

    /**
     * Constructeur par défaut de la classe Progress.
     * Permet de créer un objet Progress sans valeurs initiales.
     */
    public Progress() {}

    /**
     * Récupère l'ID du progrès.
     *
     * @return L'ID unique du progrès.
     */
    public int getId() { return id; }

    /**
     * Récupère l'ID de l'objectif auquel ce progrès est lié.
     *
     * @return L'ID de l'objectif.
     */
    public int getGoalId() { return goalId; }

    /**
     * Récupère la valeur actuelle du progrès.
     *
     * @return La valeur actuelle du progrès (par exemple, la distance parcourue, les calories brûlées).
     */
    public double getCurrentValue() { return currentValue; }

    /**
     * Récupère la date de l'enregistrement du progrès.
     *
     * @return La date de l'enregistrement du progrès (par exemple, "2025-01-12").
     */
    public String getDate() { return date; }

    /**
     * Définit l'ID du progrès.
     *
     * @param anInt L'ID du progrès.
     */
    public void setId(int anInt) {
        id = anInt;
    }

    /**
     * Définit l'ID de l'objectif auquel ce progrès est lié.
     *
     * @param anInt L'ID de l'objectif.
     */
    public void setGoalId(int anInt) {
        goalId = anInt;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet Progress.
     *
     * @return Une chaîne de caractères représentant les informations du progrès.
     */
    @Override
    public String toString() {
        return "Progress{" +
                "id=" + id +
                ", goalId=" + goalId +
                ", date='" + date + '\'' +
                ", value=" + currentValue +
                '}';
    }

    /**
     * Définit la date de l'enregistrement du progrès.
     *
     * @param string La date de l'enregistrement du progrès (par exemple, "2025-01-12").
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * Définit la valeur actuelle du progrès.
     *
     * @param aFloat La nouvelle valeur du progrès.
     */
    public void setValue(float aFloat) {
        currentValue = aFloat;
    }
}
