package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Ballot;
import com.mapare.maparevoteapp.model.entity_to_receive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_receive.Choice;

import java.util.List;

public class UniqueChoiceAdapter extends CustomAdapter<Choice> {
    private RadioButton selected = null;
    private Ballot ballot;
    private Boolean anonymous;

    public UniqueChoiceAdapter(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    public UniqueChoiceAdapter(Context context, List<Choice> choiceList, Ballot ballot) {
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
        holder.choiceField.setText(entityList.get(position).getNames().toString().replace("[", "").replace("]", ""));
        if (anonymous == null) { // If not voted
            holder.choiceField.setOnClickListener(v -> {
                if (selected != null) {
                    selected.setChecked(false);
                    pickedIds.clear();
                }
                holder.choiceField.setChecked(true);
                selected = holder.choiceField;
                pickedIds.put((int) getItemId(position), 1);
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
