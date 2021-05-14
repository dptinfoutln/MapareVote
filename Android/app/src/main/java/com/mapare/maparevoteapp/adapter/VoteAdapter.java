package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Vote;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class VoteAdapter extends CustomAdapter<Vote> {

    public VoteAdapter(Context context, List<Vote> voteList) {
        super(context, voteList);
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
            holder.labelField.setTypeface(null, Typeface.BOLD);
            holder.votemakerField = convertView.findViewById(R.id.votePublic_votemakerField);
            holder.intermediaryResultField = convertView.findViewById(R.id.votePublic_intermediaryResultField);
            holder.startDateField = convertView.findViewById(R.id.votePublic_startDateField);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String label = entityList.get(position).getLabel();
        if (label.length() > 40)
            label = label.substring(0, 40) + "...";
        holder.labelField.setText(label);

        String votemaker = entityList.get(position).getVotemaker().getFirstname() + " " + entityList.get(position).getVotemaker().getName();
        holder.votemakerField.setText(votemaker);

        String dateString = entityList.get(position).getStartDate().toString().replace("[", "").replace("]", "");
        List<String> dateList = Arrays.asList(dateString.split(","));
        dateString = dateList.get(2) + "/" + dateList.get(1) + "/" + dateList.get(0);

        dateString = dateString.replace(" ", "");

        List<String> dateList2= Arrays.asList(dateString.split("/"));
        LocalDate date = LocalDate.of(Integer.parseInt(dateList2.get(2)), Integer.parseInt(dateList2.get(1)), Integer.parseInt(dateList2.get(0)));

        if (entityList.get(position).isIntermediaryResult() || date.isBefore(LocalDate.now())) {
            holder.intermediaryResultField.setText(R.string.vote_result_textView);
            holder.intermediaryResultField.setTypeface(null, Typeface.ITALIC);
        } else {
            holder.intermediaryResultField.setVisibility(View.INVISIBLE);
        }

        holder.startDateField.setText(Html.fromHtml("<u>Date d'ouverture:</u> " + dateString, Html.FROM_HTML_MODE_COMPACT));

        return convertView;
    }
}