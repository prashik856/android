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
import com.prashik.scorer.adapters.MatchPlayerBowlingAdapter;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;
import com.prashik.scorer.models.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class InningsBowlingActivity extends AppCompatActivity {

    Match match;
    HashMap<String, String> dataFilesMap;

    int innings;
    MatchPlayerBowlingAdapter bowlingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_innings_bowling);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.innings = (int) getIntent().getSerializableExtra("innings_value");

        TextView inningsTitleView = findViewById(R.id.innings_title_ibowl);
        Team bowlingTeam;
        if(innings == 1) {
            if(this.match.isTeamABatFirst()) {
                bowlingTeam = this.match.getTeamB();
            } else {
                bowlingTeam = this.match.getTeamA();
            }
        } else {
            if(this.match.isTeamABatFirst()) {
                bowlingTeam = this.match.getTeamA();
            } else {
                bowlingTeam = this.match.getTeamB();
            }
        }

        String title = bowlingTeam.getName() + " Bowling";
        inningsTitleView.setText(title);

        ArrayList<String> temp = new ArrayList<>();
        // first, add all the players which have batted
        for(int i=0; i<bowlingTeam.getTeamPlayers().size(); i++) {
            MatchPlayer matchPlayer = bowlingTeam.getTeamPlayers().get(i);
            if(matchPlayer.getMatchPlayerBowling().isBowled()) {
                // bowled first
                temp.add(0, matchPlayer.getPlayerName());
            }
        }

        String[] battedPlayersList = temp.toArray(new String[0]);
        this.bowlingAdapter = new MatchPlayerBowlingAdapter(battedPlayersList, this.match);
        System.out.println("Getting recycler view.");
        RecyclerView recyclerView = findViewById(R.id.players_bowling_list);
        System.out.println("Setting layout manager.");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Setting adapter.");
        recyclerView.setAdapter(this.bowlingAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}