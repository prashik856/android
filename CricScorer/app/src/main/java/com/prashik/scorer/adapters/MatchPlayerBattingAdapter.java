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

public class MatchPlayerBattingAdapter extends RecyclerView.Adapter<MatchPlayerBattingAdapter.ViewHolder>{

    // with all players names, I get access to their names. using this, i can get their match player
    String[] allPlayerNames;
    Match match;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_batting, viewGroup, false);
        return new MatchPlayerBattingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String name = allPlayerNames[position];
        MatchPlayer player = this.match.getMatchPlayerObject(name);
        int runsScored = player.getMatchPlayerBatting().getRunsScored();
        int ballsPlayed = player.getMatchPlayerBatting().getBallsPlayed();
        String wicketValue = "";

        if(player.getMatchPlayerBatting().isBatted() && !player.getMatchPlayerBatting().isOut()) {
            name = name + "*";
        }

        if(!player.getMatchPlayerBatting().isOut()) {
            wicketValue = "Not out";
        } else {
            if(!player.getMatchPlayerBatting().getCoughtBy().isEmpty()) {
                wicketValue = String.format("c %s    b %s", player.getMatchPlayerBatting().getCoughtBy(),
                        player.getMatchPlayerBatting().getWicketBy());
            } else if(!player.getMatchPlayerBatting().getBowledBy().isEmpty()) {
                wicketValue = String.format("b %s", player.getMatchPlayerBatting().getWicketBy());
            } else if(!player.getMatchPlayerBatting().getRunOutBy().isEmpty()) {
                wicketValue = String.format("run out %s", player.getMatchPlayerBatting().getRunOutBy());
            } else {
                // hit wicket
                wicketValue = String.format("b %s", player.getMatchPlayerBatting().getWicketBy());
            }
        }
        double strikeRate = player.getMatchPlayerBatting().getStrikeRate();

        int fours = player.getMatchPlayerBatting().getFoursScored();
        int sixes = player.getMatchPlayerBatting().getSixesScored();
        int dots = player.getMatchPlayerBatting().getDotsPlayed();

        String id = player.getPlayer().getId();

        StringBuilder battingDetails = new StringBuilder();
        battingDetails.append("Every Ball Score - ");
        for(int i=0; i<player.getMatchPlayerBatting().getBattingDetails().size(); i++) {
            battingDetails.append(player.getMatchPlayerBatting().getBattingDetails().get(i));
            battingDetails.append(" ");
        }


        String textValue = String.format("%s   %d(%d)    SR: %.2f\n" +
                        "0s:%d   4s:%d   6s:%d\n" +
                        "%s\n" +
                        "%s",
                name, runsScored, ballsPlayed, strikeRate, dots, fours, sixes, battingDetails, wicketValue);

        viewHolder.getTextView().setText(textValue);
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
            textView = view.findViewById(R.id.single_batting_player);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public MatchPlayerBattingAdapter(String[] allPlayerNameValues, Match matchObject) {
        this.allPlayerNames = allPlayerNameValues;
        this.match = matchObject;
    }
}
