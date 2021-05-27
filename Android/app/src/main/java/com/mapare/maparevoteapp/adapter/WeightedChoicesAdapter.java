package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Ballot;
import com.mapare.maparevoteapp.model.entity_to_receive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_receive.Choice;

import java.util.List;

/**
 * The type Weighted choices adapter.
 */
public class WeightedChoicesAdapter extends CustomAdapter<Choice> {
    private Ballot ballot;
    private Boolean anonymous;

    /**
     * Instantiates a new Weighted choices adapter.
     *
     * @param context    the context
     * @param choiceList the choice list
     */
    public WeightedChoicesAdapter(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    /**
     * Instantiates a new Weighted choices adapter.
     *
     * @param context    the context
     * @param choiceList the choice list
     * @param ballot     the ballot
     */
    public WeightedChoicesAdapter(Context context, List<Choice> choiceList, Ballot ballot) {
        super(context, choiceList);
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
        TextView choiceField;
        /**
         * The Weight field.
         */
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
        holder.choiceField.setText(entityList.get(position).getNames().toString().replace("[", "").replace("]", ""));
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