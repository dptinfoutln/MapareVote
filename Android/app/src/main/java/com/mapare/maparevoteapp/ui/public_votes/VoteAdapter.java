package com.mapare.maparevoteapp.ui.public_votes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;

import java.util.List;

public class VoteAdapter extends BaseAdapter {
    private final List<Vote> voteList;
    private final LayoutInflater inflater;

    public VoteAdapter(Context context, List<Vote> voteList) {
        inflater = LayoutInflater.from(context);
        this.voteList = voteList;
    }

    @Override
    public int getCount() {
        return voteList.size();
    }

    @Override
    public Object getItem(int position) {
        return voteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView labelField;
        TextView votemakerField;
        TextView intermediaryResultField;
        TextView startDateField;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vote_list, null); // null needed here
            holder = new ViewHolder();
            holder.labelField = convertView.findViewById(R.id.votePublic_labelField);
            holder.votemakerField = convertView.findViewById(R.id.votePublic_votemakerField);
            holder.intermediaryResultField = convertView.findViewById(R.id.votePublic_intermediaryResultField);
            holder.startDateField = convertView.findViewById(R.id.votePublic_startDateField);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.labelField.setText(voteList.get(position).getLabel());

        String votemaker = voteList.get(position).getVotemaker().getFirstname() + " " + voteList.get(position).getVotemaker().getName();
        holder.votemakerField.setText(votemaker);

        if (voteList.get(position).isIntermediaryResult())
            holder.intermediaryResultField.setText("Résultats disponibles");

        holder.startDateField.setText(voteList.get(position).getStartDate().toString());

        return convertView;
    }
}