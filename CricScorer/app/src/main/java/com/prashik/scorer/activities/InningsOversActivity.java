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
import com.prashik.scorer.adapters.MatchOversAdapter;
import com.prashik.scorer.adapters.MatchPlayerBowlingAdapter;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class InningsOversActivity extends AppCompatActivity {
    Match match;
    HashMap<String, String> dataFilesMap;

    int innings;
    MatchOversAdapter matchOversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_innings_overs);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.innings = (int) getIntent().getSerializableExtra("innings_value");

        TextView inningsTitleView = findViewById(R.id.innings_title_overs);
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
        String title = bowlingTeam.getName() + " Overs";
        inningsTitleView.setText(title);

        ArrayList<String> temp = new ArrayList<>();
        for(int i=0; i<bowlingTeam.getOvers().size(); i++) {
            temp.add(Integer.toString(i));
        }

        String[] overList = temp.toArray(new String[0]);
        this.matchOversAdapter = new MatchOversAdapter(overList, bowlingTeam);
        System.out.println("Getting recycler view.");
        RecyclerView recyclerView = findViewById(R.id.overs_list);
        System.out.println("Setting layout manager.");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Setting adapter.");
        recyclerView.setAdapter(this.matchOversAdapter);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}