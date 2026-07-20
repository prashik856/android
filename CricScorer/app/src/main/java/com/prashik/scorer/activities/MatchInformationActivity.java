package com.prashik.scorer.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.MainActivity;
import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.util.Utils;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MatchInformationActivity extends AppCompatActivity {
    Match match;
    HashMap<String, String> dataFilesMap;
    String matchFileLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_information);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");
        this.matchFileLocation = (String) getIntent().getSerializableExtra("match_file_location");

        // set done or resume
        Button button = findViewById(R.id.status_button_mi);
        TextView textView = findViewById(R.id.match_summary_mi);
        if(this.match.isCompleted()) {
            button.setText("Done");
            textView.setText(this.match.getResult());
        } else {
            button.setText("Resume");
            textView.setText("Match is incomplete.");
        }

        TextView battingTeam = findViewById(R.id.batting_team_mi);
        TextView bowlingTeam = findViewById(R.id.bowling_team_mi);
        TextView firstInningsScore = findViewById(R.id.score_batting_mi);
        TextView secondInningsScore = findViewById(R.id.score_bowling_mi);
        if(this.match.isTeamABatFirst()) {
            System.out.println("Team A batting in first innings.");
            battingTeam.setText(this.match.getTeamA().getName());
            bowlingTeam.setText(this.match.getTeamB().getName());

            String score = Utils.getScore(this.match.getTeamA());
            double runRate = this.match.getTeamA().getRunRate();
            String overs = Utils.getOvers(this.match.getTeamA(), this.match);
            System.out.println(String.format("First innings score %s, runrate %.2f and overs %s",
                    score, runRate, overs));

            firstInningsScore.setText(String.format("%s  in  %s  |  RR: %.2f", score, overs, runRate));

            score = Utils.getScore(this.match.getTeamB());
            runRate = this.match.getTeamB().getRunRate();
            overs = Utils.getOvers(this.match.getTeamB(), this.match);
            System.out.println(String.format("Second innings score %s, runrate %.2f and overs %s",
                    score, runRate, overs));
            secondInningsScore.setText(String.format("%s  in  %s  |  RR: %.2f", score, overs, runRate));

        } else {
            System.out.println("Team B batting in first innings.");
            battingTeam.setText(this.match.getTeamB().getName());
            bowlingTeam.setText(this.match.getTeamA().getName());

            String score = Utils.getScore(this.match.getTeamB());
            double runRate = this.match.getTeamB().getRunRate();
            String overs = Utils.getOvers(this.match.getTeamB(), this.match);
            firstInningsScore.setText(String.format("%s  in  %s  |  RR: %.2f", score, overs, runRate));
            firstInningsScore.setText(String.format("%s  in  %s  |  RR: %.2f", score, overs, runRate));

            score = Utils.getScore(this.match.getTeamA());
            runRate = this.match.getTeamA().getRunRate();
            overs = Utils.getOvers(this.match.getTeamA(), this.match);
            System.out.println(String.format("Second innings score %s, runrate %.2f and overs %s",
                    score, runRate, overs));
            secondInningsScore.setText(String.format("%s  in  %s  |  RR: %.2f", score, overs, runRate));
        }

        // toss summary
        TextView tossSummary = findViewById(R.id.toss_summary_mi);
        tossSummary.setText(this.match.getTossDecision());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void handleFirstInningsBattingClick(View view) {
        System.out.println("Going to first innings batting summary.");
        Intent intent = new Intent(this, InningsBattingActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 1);
        startActivity(intent);
    }

    public void handleFirstInningsBowlingClick(View view) {
        System.out.println("Going to first innings bowling summary.");
        Intent intent = new Intent(this, InningsBowlingActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 1);
        startActivity(intent);
    }

    public void handleSecondInningsBattingClick(View view) {
        System.out.println("Going to second innings batting summary");
        Intent intent = new Intent(this, InningsBattingActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 2);
        startActivity(intent);
    }

    public void handleSecondInningsBowlingClick(View view) {
        System.out.println("Going to second innings bowling summary");
        Intent intent = new Intent(this, InningsBowlingActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 2);
        startActivity(intent);
    }

    public void handleFirstInningsOversClick(View view) {
        System.out.println("Going to first innings overs summary.");
        Intent intent = new Intent(this, InningsOversActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 1);
        startActivity(intent);
    }

    public void handleSecondInningsOversClick(View view) {
        System.out.println("Going to second innings overs summary.");
        Intent intent = new Intent(this, InningsOversActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        intent.putExtra("innings_value", 2);
        startActivity(intent);
    }

    public void handleDoneOrResumeClick(View view) {
        System.out.println("Going to match score activity for resuming match.");
        if(this.match.isCompleted()) {
            return;
        }
        Intent intent = new Intent(this, MatchScoreActivity.class);
        intent.putExtra("resume_match", true);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        startActivity(intent);
    }

    public void handleMatchDetailsClick(View view) {
        System.out.println("Going to match details activity.");
        Intent intent = new Intent(this, MatchDetailsActivity.class);
        intent.putExtra("match_object", this.match);
        intent.putExtra("data_files_hashmap", this.dataFilesMap);
        startActivity(intent);
    }

    public void handleDeleteMatchClick(View view) {
        System.out.println("Match delete. Confirm deletion.");

        AlertDialog.Builder builder = new AlertDialog.Builder(MatchInformationActivity.this);
        builder.setTitle("Delete this match?");
        builder.setCancelable(false);

        String[] options = {"Yes", "No"};
        final int[] optionSelected = {-1};

        builder.setSingleChoiceItems(options, optionSelected[0],
                (dialog, which) -> optionSelected[0] = which);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            if(optionSelected[0] == -1) {
                Toast.makeText(this, "You need to select yes to delete this match.", Toast.LENGTH_LONG).show();
                return;
            }

            String answer = options[optionSelected[0]];
            System.out.println("Option selected is: " + answer);

            if(answer.equals("Yes")) {
                Utils.deleteFile(this.matchFileLocation);

                Intent intent = new Intent(this, PreviousMatchesActivity.class);
                intent.putExtra("data_files_hashmap", dataFilesMap);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Clear", (dialog, which) -> {
            optionSelected[0] = -1;
        });

        builder.show();
    }
}