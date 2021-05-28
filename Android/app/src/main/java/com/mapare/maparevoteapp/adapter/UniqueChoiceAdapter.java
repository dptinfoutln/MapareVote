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

/**
 * The type Unique choice adapter.
 */
public class UniqueChoiceAdapter extends CustomAdapter<Choice> {
    private RadioButton selected = null;
    /**
     * Instantiates a new Unique choice adapter.
     *
     * @param context    the context
     * @param choiceList the choice list
     */
    public UniqueChoiceAdapter(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    /**
     * Instantiates a new Unique choice adapter.
     *
     * @param context    the context
     * @param choiceList the choice list
     * @param ballot     the ballot
     */
    public UniqueChoiceAdapter(Context context, List<Choice> choiceList, Ballot ballot) {
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

            if (Boolean.FALSE.equals(anonymous)) {
                for (BallotChoice bc : ballot.getChoices()) {
                    if (bc.getChoice().getId() == getItemId(position)) {
                        holder.choiceField.setChecked(true);
                        break;
                    }
                }
            }
            holder.choiceField.setEnabled(false);
        }

        return convertView;
    }
}
