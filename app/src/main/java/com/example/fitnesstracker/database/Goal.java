package com.example.fitnesstracker.database;

/**
 * Représente un objectif de suivi de progrès dans le cadre de l'application.
 * Un objectif est défini par son type , sa valeur cible,
 * sa date de début, sa date de fin et son statut (en cours, complété, etc.).
 * Il peut être associé à un utilisateur spécifique.
 */
public class Goal {

    private int goalId;
    private int userId;
    private String goalType;
    private float targetValue;
    private String startDate;
    private String endDate;
    private String status;

    /**
     * Constructeur pour l'ajout d'un nouvel objectif sans ID.
     *
     * @param goalType     Type de l'objectif (par exemple, "distance", "calories").
     * @param targetValue  Valeur cible de l'objectif.
     * @param startDate    Date de début de l'objectif.
     * @param endDate      Date de fin de l'objectif.
     * @param status       Statut de l'objectif (par exemple, "En cours", "Complété").
     * @param userId       ID de l'utilisateur associé à l'objectif.
     */
    public Goal(String goalType, float targetValue, String startDate, String endDate, String status, int userId) {
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.userId = userId;
    }

    /**
     * Constructeur pour la modification d'un objectif existant avec ID.
     *
     * @param goalId       ID de l'objectif.
     * @param userId       ID de l'utilisateur associé à l'objectif.
     * @param goalType     Type de l'objectif (par exemple, "distance", "calories").
     * @param targetValue  Valeur cible de l'objectif.
     * @param startDate    Date de début de l'objectif.
     * @param endDate      Date de fin de l'objectif.
     * @param status       Statut de l'objectif (par exemple, "En cours", "Complété").
     */
    public Goal(int goalId, int userId, String goalType, float targetValue, String startDate, String endDate, String status) {
        this.goalId = goalId;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.userId = userId;
    }

    /**
     * Constructeur pour créer un objectif sans ID.
     *
     * @param goalType     Type de l'objectif (par exemple, "distance", "calories").
     * @param targetValue  Valeur cible de l'objectif.
     * @param startDate    Date de début de l'objectif.
     * @param endDate      Date de fin de l'objectif.
     * @param status       Statut de l'objectif (par exemple, "En cours", "Complété").
     */
    public Goal(String goalType, float targetValue, String startDate, String endDate, String status) {
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    /**
     * Constructeur par défaut.
     */
    public Goal() {
    }

    /**
     * Récupère l'ID de l'objectif.
     *
     * @return L'ID de l'objectif.
     */
    public int getGoalId() {
        return goalId;
    }

    /**
     * Définit l'ID de l'objectif.
     *
     * @param goalId L'ID de l'objectif.
     */
    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    /**
     * Récupère le type de l'objectif.
     *
     * @return Le type de l'objectif.
     */
    public String getGoalType() {
        return goalType;
    }

    /**
     * Définit le type de l'objectif.
     *
     * @param goalType Le type de l'objectif.
     */
    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    /**
     * Récupère la valeur cible de l'objectif.
     *
     * @return La valeur cible de l'objectif.
     */
    public float getTargetValue() {
        return targetValue;
    }

    /**
     * Définit la valeur cible de l'objectif.
     *
     * @param targetValue La valeur cible de l'objectif.
     */
    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * Récupère la date de début de l'objectif.
     *
     * @return La date de début de l'objectif.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Définit la date de début de l'objectif.
     *
     * @param startDate La date de début de l'objectif.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Récupère la date de fin de l'objectif.
     *
     * @return La date de fin de l'objectif.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Définit la date de fin de l'objectif.
     *
     * @param endDate La date de fin de l'objectif.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Récupère le statut de l'objectif.
     *
     * @return Le statut de l'objectif.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Définit le statut de l'objectif.
     *
     * @param status Le statut de l'objectif.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Récupère l'ID de l'utilisateur associé à cet objectif.
     *
     * @return L'ID de l'utilisateur.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Définit l'ID de l'utilisateur associé à cet objectif.
     *
     * @param userId L'ID de l'utilisateur.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int anInt) {
        goalId=anInt;
    }
}
