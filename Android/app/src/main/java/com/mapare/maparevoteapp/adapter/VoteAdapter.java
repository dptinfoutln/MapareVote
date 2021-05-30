package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Vote;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

/**
 * The type Vote adapter.
 */
public class VoteAdapter extends CustomAdapter<Vote> {

    /**
     * Instantiates a new Vote adapter.
     *
     * @param context  the context
     * @param voteList the vote list
     */
    public VoteAdapter(Context context, List<Vote> voteList) {
        super(context, voteList);
    }

    /**
     * The type View holder.
     */
    public static class ViewHolder {
        /**
         * The Label field.
         */
        TextView labelField;
        /**
         * The Votemaker field.
         */
        TextView votemakerField;
        /**
         * The Intermediary result field.
         */
        TextView intermediaryResultField;
        /**
         * The Start date field.
         */
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

        LocalDate endDate;
        if (entityList.get(position).getEndDate() != null) {
            String dateString2 = entityList.get(position).getEndDate().toString().replace("[", "").replace("]", "");
            List<String> dateList2 = Arrays.asList(dateString2.split(","));
            dateString2 = dateList2.get(2) + "/" + dateList2.get(1) + "/" + dateList2.get(0);
            dateString2 = dateString2.replace(" ", "");
            List<String> endDateList = Arrays.asList(dateString2.split("/"));
            endDate = LocalDate.of(Integer.parseInt(endDateList.get(2)), Integer.parseInt(endDateList.get(1)), Integer.parseInt(endDateList.get(0)));
        } else
            endDate = LocalDate.now(ZoneId.of("GMT")).plusDays(1);

        if (entityList.get(position).isIntermediaryResult() || endDate.isBefore(LocalDate.now(ZoneId.of("GMT")).plusDays(1))) {
            holder.intermediaryResultField.setText(R.string.vote_result_text_view);
            holder.intermediaryResultField.setTypeface(null, Typeface.ITALIC);
        } else {
            holder.intermediaryResultField.setVisibility(View.INVISIBLE);
        }

        holder.startDateField.setText(Html.fromHtml("<u>Date d'ouverture:</u> " + dateString, Html.FROM_HTML_MODE_COMPACT));

        return convertView;
    }
}