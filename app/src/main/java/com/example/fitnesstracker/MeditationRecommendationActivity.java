package com.example.fitnesstracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.Adapters.MeditationRecommendationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activité affichant une liste de recommandations pour la méditation.
 * L'utilisateur peut consulter différentes suggestions pour améliorer sa pratique de la méditation.
 *

 */
public class MeditationRecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView pour afficher les recommandations
    private MeditationRecommendationAdapter adapter;  // Adapter pour lier les données avec le RecyclerView
    private List<String> recommendations;  // Liste des recommandations de méditation

    /**
     * Méthode appelée lors de la création de l'activité.
     * Elle initialise les données et configure l'affichage des recommandations de méditation.
     *
     * @param savedInstanceState L'état sauvegardé de l'activité, utilisé pour restaurer l'état précédent de l'interface.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_recommendation);

        recyclerView = findViewById(R.id.recycler_view_meditation);  // Lier le RecyclerView à la vue

        // Initialiser les données des recommandations de méditation
        recommendations = new ArrayList<>();
        recommendations.add("Prenez quelques minutes chaque matin pour respirer profondément et vous recentrer avant de commencer votre journée.");
        recommendations.add("Essayez de méditer en pleine conscience pendant 10 minutes avant de vous coucher pour apaiser votre esprit.");
        recommendations.add("Écoutez de la musique calme ou des sons naturels pour accompagner votre session de méditation.");
        recommendations.add("Commencez avec des méditations guidées si vous êtes débutant pour vous aider à rester concentré.");
        recommendations.add("Pratiquez la méditation en marchant pour intégrer la pleine conscience à vos déplacements quotidiens.");
        recommendations.add("Méditez régulièrement pour améliorer votre bien-être mental et physique à long terme.");
        recommendations.add("Lorsque des pensées distrayantes apparaissent, acceptez-les sans jugement et ramenez doucement votre attention à votre respiration.");

        // Initialiser l'adapter et configurer le RecyclerView
        adapter = new MeditationRecommendationAdapter(recommendations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Utilisation d'un LinearLayoutManager pour afficher les éléments en liste
        recyclerView.setAdapter(adapter);  // Lier l'adapter au RecyclerView
    }
}
