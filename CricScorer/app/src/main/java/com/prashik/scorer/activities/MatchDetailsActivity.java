package com.prashik.scorer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
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

        TextView matchActivities = findViewById(R.id.match_activities_md);
        StringBuilder value = new StringBuilder();
        for(int i=0; i<this.match.getActivities().size(); i++) {
            value.append(this.match.getActivities().get(i));
            if(!(i == this.match.getActivities().size() - 1)) {
                value.append(",  ");
            }
        }
        matchActivities.setText(value.toString());

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
}