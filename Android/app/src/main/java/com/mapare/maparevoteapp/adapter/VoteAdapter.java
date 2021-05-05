package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;

import java.util.List;

public class VoteAdapter extends CustomAdapter<Vote> {
    private final LayoutInflater inflater;

    public VoteAdapter(Context context, List<Vote> voteList) {
        super(voteList);
        inflater = LayoutInflater.from(context);
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
            holder = (ViewHolder) convertView.getTag();
        }
        holder.labelField.setText(entityList.get(position).getLabel());

        String votemaker = entityList.get(position).getVotemaker().getFirstname() + " " + entityList.get(position).getVotemaker().getName();
        holder.votemakerField.setText(votemaker);

        if (entityList.get(position).isIntermediaryResult())
            holder.intermediaryResultField.setText("Résultats disponibles");

        holder.startDateField.setText(entityList.get(position).getStartDate().toString());

        return convertView;
    }
}