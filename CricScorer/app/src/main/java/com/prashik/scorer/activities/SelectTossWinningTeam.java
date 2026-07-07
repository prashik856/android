package com.prashik.scorer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

import java.util.HashMap;

public class SelectTossWinningTeam extends AppCompatActivity {
    HashMap<String, String> dataFilesMap;
    Match match;
    int selectedAnswer = -1;
    String chosenAnswer = "";
    String[] options = {"Bat First", "Bowl First"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_toss_winning_team);

        this.dataFilesMap = (HashMap<String, String>) getIntent().getSerializableExtra("data_files_hashmap");
        this.match = (Match) getIntent().getSerializableExtra("match_object");

        Button teamAButton = findViewById(R.id.team_a_button_tw);
        teamAButton.setText(this.match.getTeamA().getName());
        TextView squadTeamA = findViewById(R.id.show_full_squad_team_a);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s(C), ", this.match.getTeamA().getCaptainName()));
        for(int i=0; i<this.match.getTeamA().getTeamPlayers().size(); i++) {
            String playerName = this.match.getTeamA().getTeamPlayers().get(i).getPlayer().getFullName();
            if(playerName.equals(this.match.getTeamA().getCaptainName())) {
                continue;
            }
            stringBuilder.append(playerName);
            if(!(i == this.match.getTeamA().getTeamPlayers().size() - 1)) {
                stringBuilder.append(", ");
            }
        }
        squadTeamA.setText(stringBuilder.toString());
        System.out.println("Team A Squad: " + stringBuilder);

        Button teamBButton = findViewById(R.id.team_b_button_tw);
        teamBButton.setText(this.match.getTeamB().getName());
        TextView squadTeamB = findViewById(R.id.show_full_squad_team_b);
        stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s(C), ", this.match.getTeamB().getCaptainName()));
        for(int i=0; i<this.match.getTeamB().getTeamPlayers().size(); i++) {
            String playerName = this.match.getTeamB().getTeamPlayers().get(i).getPlayer().getFullName();
            if(playerName.equals(this.match.getTeamB().getCaptainName())) {
                continue;
            }
            stringBuilder.append(playerName);
            if(!(i == this.match.getTeamB().getTeamPlayers().size() - 1)) {
                stringBuilder.append(", ");
            }
        }
        squadTeamB.setText(stringBuilder.toString());
        System.out.println("Team B Squad: " + stringBuilder);

        teamAButton.setOnClickListener(v -> {
            System.out.println("Setting up team a button onclick");
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectTossWinningTeam.this);
            builder.setTitle("Choose what you want to do?");
            builder.setCancelable(false);

            builder.setSingleChoiceItems(options, selectedAnswer, (dialog, which) -> selectedAnswer = which);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                if(selectedAnswer != -1) {
                    chosenAnswer = options[selectedAnswer];
                }
                System.out.println("Chosen answer is: " + chosenAnswer);
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                selectedAnswer = -1;
                chosenAnswer = "";
            });

            builder.setOnDismissListener(dialog -> {
                if(selectedAnswer == -1) {
                    Toast.makeText(this, "Please select what you want to do after winning toss.", Toast.LENGTH_LONG).show();
                    return;
                }

                this.match.setTeamAToss(true);
                this.match.setTeamBToss(false);
                this.match.setTossDecision(this.match.getTeamA().getName() + " won the toss and chose to " + chosenAnswer);
                if(selectedAnswer == 0) {
                    this.match.setTeamABatFirst(true);
                    this.match.setTeamBBatFirst(false);
                } else {
                    this.match.setTeamBBatFirst(true);
                    this.match.setTeamABatFirst(false);
                }
                this.match.setBattingAndBowlingTeamNames();
                System.out.println(this.match.getTeamA().getName() + " won the toss and chose to " + chosenAnswer);
                System.out.println("Team A Bat First: " + this.match.isTeamABatFirst());
                System.out.println("Team B Bat First " + this.match.isTeamBBatFirst());

                Intent intent = new Intent(this, MatchPropertiesActivity.class);
                intent.putExtra("data_files_hashmap", dataFilesMap);
                intent.putExtra("match_object", this.match);
                startActivity(intent);
            });

            builder.show();
        });

        teamBButton.setOnClickListener(v -> {
            System.out.println("Setting up team b button onclick");
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectTossWinningTeam.this);
            builder.setTitle("Choose what you want to do?");

            builder.setSingleChoiceItems(options, selectedAnswer, (dialog, which) -> selectedAnswer = which);

            builder.setPositiveButton("Ok", (dialog, which) -> {
                if(selectedAnswer != -1) {
                    chosenAnswer = options[selectedAnswer];
                }
                System.out.println("Chosen answer is: " + chosenAnswer);
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear", (dialog, which) -> {
                selectedAnswer = -1;
                chosenAnswer = "";
            });

            builder.setOnDismissListener(dialog -> {
                if(selectedAnswer == -1) {
                    Toast.makeText(this, "Please select what you want to do after winning toss.", Toast.LENGTH_LONG).show();
                    return;
                }

                this.match.setTeamBToss(true);
                this.match.setTeamAToss(false);
                this.match.setTossDecision(this.match.getTeamB().getName() + " won the toss and chose to " + chosenAnswer);
                if(selectedAnswer == 0) {
                    this.match.setTeamBBatFirst(true);
                    this.match.setTeamABatFirst(false);
                } else {
                    this.match.setTeamABatFirst(true);
                    this.match.setTeamBBatFirst(false);
                }
                this.match.setBattingAndBowlingTeamNames();
                System.out.println(this.match.getTeamB().getName() + " won the toss and chose to " + chosenAnswer);
                System.out.println("Team A Bat First: " + this.match.isTeamABatFirst());
                System.out.println("Team B Bat First " + this.match.isTeamBBatFirst());

                Intent intent = new Intent(this, MatchPropertiesActivity.class);
                intent.putExtra("data_files_hashmap", this.dataFilesMap);
                intent.putExtra("match_object", this.match);
                startActivity(intent);
            });

            builder.show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}