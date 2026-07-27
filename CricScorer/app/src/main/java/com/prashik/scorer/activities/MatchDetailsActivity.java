package com.prashik.scorer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.adapters.MatchActivityAdapter;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Team;

public class MatchDetailsActivity extends AppCompatActivity {

    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_details);

        this.match = (Match) getIntent().getSerializableExtra("match_object");

        TextView teamName = findViewById(R.id.team_a_name_md);
        teamName.setText(this.match.getTeamA().getName());

        teamName = findViewById(R.id.team_b_name_md);
        teamName.setText(this.match.getTeamB().getName());

        TextView teamPlayers = findViewById(R.id.team_a_players_md);
        teamPlayers.setText(getTeamPlayerString(this.match.getTeamA()));

        teamPlayers = findViewById(R.id.team_b_players_md);
        teamPlayers.setText(getTeamPlayerString(this.match.getTeamB()));

        MatchActivityAdapter matchActivityAdapter = new MatchActivityAdapter(this.match.getActivities().toArray(new String[0]));
        RecyclerView recyclerView = findViewById(R.id.match_activity_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Setting adapter.");
        recyclerView.setAdapter(matchActivityAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public String getTeamPlayerString(Team team) {
        StringBuilder teamPlayersBuilder = new StringBuilder();
        teamPlayersBuilder.append(team.getCaptainName());
        teamPlayersBuilder.append("(C),  ");

        for(int i=0; i<team.getPlayerNames().size(); i++) {
            String player = team.getPlayerNames().get(i);
            if(!team.getCaptainName().equals(player)) {
                teamPlayersBuilder.append(player);
                if(i != team.getPlayerNames().size() - 1) {
                    teamPlayersBuilder.append(",  ");
                }
            }
        }

        return teamPlayersBuilder.toString();
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}