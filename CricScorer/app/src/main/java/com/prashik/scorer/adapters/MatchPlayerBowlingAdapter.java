package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.MatchPlayer;

import java.util.ArrayList;

public class MatchPlayerBowlingAdapter extends RecyclerView.Adapter<MatchPlayerBowlingAdapter.ViewHolder>{
    // with all players names, I get access to their names. using this, i can get their match player
    String[] allPlayerNames;
    Match match;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String name = allPlayerNames[position];
        MatchPlayer player = this.match.getMatchPlayerObject(name);

        int overs = player.getMatchPlayerBowling().getLegalDeliveriesBowled() / 6;
        int balls = player.getMatchPlayerBowling().getLegalDeliveriesBowled() % 6;
        int runs = player.getMatchPlayerBowling().getRunsConceded();
        int wickets = player.getMatchPlayerBowling().getWicketsTaken();
        double economy = player.getMatchPlayerBowling().getEconomy();

        int maidens = player.getMatchPlayerBowling().getMaidenOverBowled();
        ArrayList<String> maidenOverTo = player.getMatchPlayerBowling().getMaidenBowledTo();

        int wides = player.getMatchPlayerBowling().getWideBalls();
        int noBalls = player.getMatchPlayerBowling().getNoBalls();
        int extras = player.getMatchPlayerBowling().getExtrasConceded();

        int dots = player.getMatchPlayerBowling().getDotsConceded();
        int fours = player.getMatchPlayerBowling().getFoursConceded();
        int sixes = player.getMatchPlayerBowling().getSixesConceded();

        ArrayList<Integer> matchOverBowled = player.getMatchPlayerBowling().getOverBowled();
        ArrayList<String> bowledPlayers = player.getMatchPlayerBowling().getBowledPlayers();
        ArrayList<String> wicketsTakenPlayers = player.getMatchPlayerBowling().getWicketsTakenPlayers();

        ArrayList<String> bowlingDetails = player.getMatchPlayerBowling().getBowlingDetails();

        StringBuilder maidenText = new StringBuilder();
        maidenText.append("M:");
        maidenText.append(maidens);
        maidenText.append("\tBowledTo: ");
        for(int i=0; i<maidenOverTo.size(); i++) {
            maidenText.append(maidenOverTo.get(i));
            maidenText.append("\t");
        }

        StringBuilder matchOverBowledBuilder = new StringBuilder();
        matchOverBowledBuilder.append("Match Overs: ");
        for(int i=0; i<matchOverBowled.size(); i++) {
            matchOverBowledBuilder.append(matchOverBowled.get(i));
            matchOverBowledBuilder.append(" ");
        }

        StringBuilder wicketsTakenPlayersBuilder = new StringBuilder();
        wicketsTakenPlayersBuilder.append("Wickets Taken Players: ");
        for(int i=0; i<wicketsTakenPlayers.size(); i++) {
            wicketsTakenPlayersBuilder.append(wicketsTakenPlayers.get(i));
            if(i != wicketsTakenPlayers.size() - 1) {
                wicketsTakenPlayersBuilder.append(", ");
            }
        }

        StringBuilder bowledPlayersBuilder = new StringBuilder();
        bowledPlayersBuilder.append("Bowled Players: ");
        for(int i=0; i<bowledPlayers.size(); i++) {
            bowledPlayersBuilder.append(bowledPlayers.get(i));
            if(i != bowledPlayers.size() - 1) {
                bowledPlayersBuilder.append(", ");
            }
        }

        StringBuilder bowlingDetailsBuilder = new StringBuilder();
        bowlingDetailsBuilder.append("Every ball score - ");
        for(int i=0; i<bowlingDetails.size(); i++) {
            bowlingDetailsBuilder.append(bowlingDetails.get(i));
            bowlingDetailsBuilder.append(" ");
        }

        String text = String.format("%s\n" +
                        "%d.%d\tR:%d\tW:%d\tECON:%.2f\n" +
                        "%s\n" +
                        "WB:%d\tNB:%d\tExtras:%d\n" +
                        "0s:%d\t4s:%d\t6s:%d\n" +
                        "%s\n" +
                        "%s\n" +
                        "%s\n" +
                        "%s"
                , name,
                overs, balls, runs, wickets, economy,
                maidenText,
                wides, noBalls, extras,
                dots, fours, sixes,
                matchOverBowledBuilder,
                wicketsTakenPlayersBuilder,
                bowledPlayersBuilder,
                bowlingDetailsBuilder);

        String id = player.getPlayer().getId();

        viewHolder.getTextView().setText(text);
        viewHolder.getTextView().setContentDescription(id);
    }

    @Override
    public int getItemCount() {
        return allPlayerNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.single_player_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public MatchPlayerBowlingAdapter(String[] allPlayerNameValues, Match matchObject) {
        this.allPlayerNames = allPlayerNameValues;
        this.match = matchObject;
    }
}
