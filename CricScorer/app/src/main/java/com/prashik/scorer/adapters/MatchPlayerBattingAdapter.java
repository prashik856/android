package com.prashik.scorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashik.scorer.R;
import com.prashik.scorer.models.Match;

public class MatchPlayerBattingAdapter extends RecyclerView.Adapter<MatchPlayerBattingAdapter.ViewHolder>{

    // with all players names, I get access to their names. using this, i can get their match player
    String[] allPlayerNames;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new MatchPlayerBattingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText("");
        viewHolder.getTextView().setContentDescription("");
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

    public MatchPlayerBattingAdapter(String[] allPlayerNameValues, Match matchObject) {
        this.allPlayerNames = allPlayerNameValues;
    }
}
