package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;
import com.prashik.scorer.models.Over;
import com.prashik.scorer.models.Team;

import java.util.ArrayList;

public class MatchOversAdapter extends RecyclerView.Adapter<MatchOversAdapter.ViewHolder>{
    String[] allOvers;
    Team bowlingTeam;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_overs, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Over over = bowlingTeam.getOvers().get(position);

        int overNumber = over.getMatchOverId() + 1;
        String bowledBy = over.getPlayerName();

        int deliveries = over.getLegalDeliveries();
        int wides = over.getWides();
        int noBalls = over.getNoBalls();
        int dots = over.getDots();

        // runs
        int wickets = over.getWickets();
        int runs = over.getRuns();
        int byes = over.getByes();
        int extras = over.getExtras();

        String maidenText = "No";
        if(over.isMaiden()) {
            maidenText = "Yes";
        }

        String completed = "No";
        if(over.isOverCompleted()) {
            completed = "Yes";
        }

        ArrayList<String> overDetails = over.getOverSummary();
        StringBuilder overDetailsBuilder = new StringBuilder();
        overDetailsBuilder.append("Every ball : ");
        for(int i=0; i<overDetails.size(); i++) {
            overDetailsBuilder.append(overDetails.get(i));
            overDetailsBuilder.append(" ");
        }

        String text = String.format("Match Over: %d\n" +
                "Bowled By:  %s\n" +
                "Balls:%d   WB:%d   NB:%d   Dots:%d\n" +
                "Wkts:%d   Runs:%d   Byes:%d   Extras:%d\n" +
                "Maiden Over: %s    Over Completed: %s\n" +
                "%s",
                overNumber,
                bowledBy,
                deliveries, wides, noBalls, dots,
                wickets, runs, byes, extras,
                maidenText, completed,
                overDetailsBuilder);

        viewHolder.getTextView().setText(text);
        viewHolder.getTextView().setContentDescription(Integer.toString(position));
    }


    @Override
    public int getItemCount() {
        return allOvers.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.single_over_item);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public MatchOversAdapter(String[] allOversValues, Team bowlingTeamObject) {
        this.allOvers = allOversValues;
        this.bowlingTeam = bowlingTeamObject;
    }

}
