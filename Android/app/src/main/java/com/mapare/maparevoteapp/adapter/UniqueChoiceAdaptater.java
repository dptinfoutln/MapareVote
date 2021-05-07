package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Ballot;
import com.mapare.maparevoteapp.model.entity_to_reveive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_reveive.Choice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UniqueChoiceAdaptater extends CustomAdapter<Choice> {
    private RadioButton selected = null;
    private Ballot ballot;
    private Boolean anonymous;

    public UniqueChoiceAdaptater(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    public UniqueChoiceAdaptater(Context context, List<Choice> choiceList, Ballot ballot) {
        super(context, choiceList);
        if (ballot == null)
            this.anonymous = true;
        else {
            this.ballot = ballot;
            anonymous = false;
        }
    }

    public static class ViewHolder {
        RadioButton choiceField;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choice_radio_button, null); // null needed here
            holder = new ViewHolder();
            holder.choiceField = convertView.findViewById(R.id.vote_uniqueChoice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.choiceField.setText(entityList.get(position).getNames().toString());
        Log.i("anonymous", anonymous+"");
        if (anonymous == null) { // If not voted
            holder.choiceField.setOnClickListener(v -> {
                if (selected != null)
                    selected.setChecked(false);
                holder.choiceField.setChecked(true);
                selected = holder.choiceField;
                pickedIds = Collections.singletonList((int) getItemId(position));
            });
        } else {
            if (position == 0) // bug ?
                holder.choiceField.setChecked(false);

            if (!anonymous) {
                for (BallotChoice bc : ballot.getChoices()) {
                    if (bc.getChoice().getId() == getItemId(position)) {
                        holder.choiceField.setChecked(true);
                        break;
                    }
                }
            }
            //else if TODO : print the fact that the vote is anonymous
            holder.choiceField.setEnabled(false);
        }

        return convertView;
    }


}
