package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Ballot;
import com.mapare.maparevoteapp.model.entity_to_receive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_receive.Choice;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Multiple choices adapter.
 */
public class MultipleChoicesAdapter extends CustomAdapter<Choice> {
    private final List<CheckBox> selected = new ArrayList<>();
    private  Ballot ballot;
    private final int maxChoices;
    private int count = 0;
    private Boolean anonymous;

    /**
     * Instantiates a new Multiple choices adapter.
     *
     * @param context    the context
     * @param entityList the entity list
     * @param maxChoices the max choices
     */
    public MultipleChoicesAdapter(Context context, List<Choice> entityList, int maxChoices) {
        super(context, entityList);
        this.maxChoices = maxChoices;
    }

    /**
     * Instantiates a new Multiple choices adapter.
     *
     * @param context    the context
     * @param entityList the entity list
     * @param maxChoices the max choices
     * @param ballot     the ballot
     */
    public MultipleChoicesAdapter(Context context, List<Choice> entityList, int maxChoices, Ballot ballot) {
        super(context, entityList);
        this.maxChoices = maxChoices;
        if (ballot == null)
            this.anonymous = true;
        else {
            this.ballot = ballot;
            anonymous = false;
        }

    }

    /**
     * The type View holder.
     */
    public static class ViewHolder {
        /**
         * The Choice field.
         */
        CheckBox choiceField;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.choice_check_box, null); // null needed here
            holder = new ViewHolder();
            holder.choiceField = convertView.findViewById(R.id.vote_multipleChoices);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.choiceField.setText(entityList.get(position).getNames().toString().replace("[", "").replace("]", ""));
        if (anonymous == null) { // If not voted
            holder.choiceField.setOnClickListener(v -> {
                if (selected.contains(holder.choiceField)) {
                    pickedIds.remove((int)getItemId(position));
                    selected.remove(holder.choiceField);
                    holder.choiceField.setChecked(false);
                    count--;
                } else {
                    if (count < maxChoices) {
                        count++;
                    } else {
                        selected.get(0).setChecked(false);
                        pickedIds.remove(pickedIds.keySet().toArray()[0]);
                        selected.remove(0);
                    }
                    pickedIds.put((int) getItemId(position), 1);
                    selected.add(holder.choiceField);
                    holder.choiceField.setChecked(true);
                }
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
