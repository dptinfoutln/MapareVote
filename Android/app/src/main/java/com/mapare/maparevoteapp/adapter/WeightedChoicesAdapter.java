package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Ballot;
import com.mapare.maparevoteapp.model.entity_to_reveive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_reveive.Choice;

import java.util.List;

public class WeightedChoicesAdapter extends CustomAdapter<Choice> {
    private Ballot ballot;
    private Boolean anonymous;

    public WeightedChoicesAdapter(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    public WeightedChoicesAdapter(Context context, List<Choice> choiceList, Ballot ballot) {
        super(context, choiceList);
        if (ballot == null)
            this.anonymous = true;
        else {
            this.ballot = ballot;
            anonymous = false;
        }
    }

    public static class ViewHolder {
        TextView choiceField;
        NumberPicker weightField;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choice_list, null); // null needed here
            holder = new ViewHolder();
            holder.choiceField = convertView.findViewById(R.id.vote_choice);
            holder.weightField = convertView.findViewById(R.id.vote_weightPicker);
            holder.weightField.setMinValue(1);
            holder.weightField.setMaxValue(entityList.size());
            pickedIds.put((int)getItemId(position), holder.weightField.getMinValue()); // Because min value is the default value
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.choiceField.setText(entityList.get(position).getNames().toString());
        if (anonymous == null) { // If not voted
            holder.weightField.setOnValueChangedListener((picker, oldVal, newVal) -> pickedIds.put((int)getItemId(position), newVal));
        } else {
            if (!anonymous) {
                for (BallotChoice bc : ballot.getChoices()) {
                    if (bc.getChoice().getId() == getItemId(position)) {
                        holder.weightField.setValue(bc.getWeight());
                        break;
                    }
                }
            }
            //else if TODO : print the fact that the vote is anonymous
            holder.weightField.setEnabled(false);
        }

        return convertView;
    }

}