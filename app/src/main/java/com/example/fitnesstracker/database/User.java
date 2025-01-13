package com.example.fitnesstracker.database;

/**
 * Représente un utilisateur dans l'application de suivi de fitness.
 * Cette classe contient des informations personnelles sur l'utilisateur, telles que son identifiant, son nom d'utilisateur,
 * son email, son âge, son sexe, son poids et sa taille.
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private int age;
    private String gender;
    private float weight;
    private float height;

    /**
     * Constructeur pour initialiser un utilisateur avec les informations personnelles.
     *
     * @param userId L'ID unique de l'utilisateur.
     * @param username Le nom d'utilisateur.
     * @param email L'email de l'utilisateur.
     * @param age L'âge de l'utilisateur.
     * @param gender Le sexe de l'utilisateur.
     * @param weight Le poids de l'utilisateur en kilogrammes.
     * @param height La taille de l'utilisateur en mètres.
     */
    public User(int userId, String username, int age, String gender, float weight, float height) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    /**
     * Récupère l'ID de l'utilisateur.
     *
     * @return L'ID unique de l'utilisateur.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Modifie l'ID de l'utilisateur.
     *
     * @param userId L'ID de l'utilisateur à définir.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Récupère le nom d'utilisateur.
     *
     * @return Le nom d'utilisateur de l'utilisateur.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modifie le nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à définir.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Récupère l'email de l'utilisateur.
     *
     * @return L'email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Modifie l'email de l'utilisateur.
     *
     * @param email L'email à définir.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Récupère l'âge de l'utilisateur.
     *
     * @return L'âge de l'utilisateur.
     */
    public int getAge() {
        return age;
    }

    /**
     * Modifie l'âge de l'utilisateur.
     *
     * @param age L'âge à définir.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Récupère le sexe de l'utilisateur.
     *
     * @return Le sexe de l'utilisateur.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Modifie le sexe de l'utilisateur.
     *
     * @param gender Le sexe à définir.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Récupère le poids de l'utilisateur.
     *
     * @return Le poids de l'utilisateur en kilogrammes.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Modifie le poids de l'utilisateur.
     *
     * @param weight Le poids à définir.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Récupère la taille de l'utilisateur.
     *
     * @return La taille de l'utilisateur en mètres.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Modifie la taille de l'utilisateur.
     *
     * @param height La taille à définir.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'utilisateur.
     *
     * @return Une chaîne contenant toutes les informations de l'utilisateur.
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }
}
