package com.prashik.scorer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.adapters.MatchPlayerBattingAdapter;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class InningsBattingActivity extends AppCompatActivity {

    Match match;
    HashMap<String, String> dataFilesMap;

    int innings;
    MatchPlayerBattingAdapter battingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_innings_batting);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.innings = (int) getIntent().getSerializableExtra("innings_value");

        TextView inningsTitleView = findViewById(R.id.innings_title_ibat);

        Team battingTeam;
        if(innings == 1) {
            if(this.match.isTeamABatFirst()) {
                battingTeam = this.match.getTeamA();
            } else {
                battingTeam = this.match.getTeamB();
            }
        } else {
            if(this.match.isTeamABatFirst()) {
                battingTeam = this.match.getTeamB();
            } else {
                battingTeam = this.match.getTeamA();
            }
        }

        String title = battingTeam.getName() + " Batting";
        inningsTitleView.setText(title);

        ArrayList<String> temp = new ArrayList<>();
        // first, add all the players which have batted
        for(int i=0; i<battingTeam.getTeamPlayers().size(); i++) {
            MatchPlayer matchPlayer = battingTeam.getTeamPlayers().get(i);
            if(matchPlayer.getMatchPlayerBatting().isBatted()) {
                // batted first
                temp.add(0, matchPlayer.getPlayerName());
            } else {
                // not batted last
                temp.add(matchPlayer.getPlayerName());
            }
        }

        String[] battedPlayersList = temp.toArray(new String[0]);
        this.battingAdapter = new MatchPlayerBattingAdapter(battedPlayersList, this.match);
        System.out.println("Getting recycler view.");
        RecyclerView recyclerView = findViewById(R.id.match_activity_display);
        System.out.println("Setting layout manager.");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Setting adapter.");
        recyclerView.setAdapter(this.battingAdapter);

        TextView fow = findViewById(R.id.fall_of_wickets);
        StringBuilder fallOfWickets = new StringBuilder();
        for(int i=0; i<battingTeam.getFallOfWickets().size(); i++) {
            fallOfWickets.append(battingTeam.getFallOfWickets().get(i));
            fallOfWickets.append("    ");
        }
        fow.setText(String.format("FOW: %s", fallOfWickets));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}