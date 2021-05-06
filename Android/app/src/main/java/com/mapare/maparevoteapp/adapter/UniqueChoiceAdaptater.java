package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_reveive.Choice;

import java.util.List;

public class UniqueChoiceAdaptater extends CustomAdapter<Choice> {
    private RadioButton selected = null;
    private List<BallotChoice> alreadyPickedChoices;

    public UniqueChoiceAdaptater(Context context, List<Choice> choiceList) {
        super(context, choiceList);
    }

    public UniqueChoiceAdaptater(Context context, List<Choice> choiceList, List<BallotChoice> alreadyPickedChoices) {
        super(context, choiceList);
        this.alreadyPickedChoices = alreadyPickedChoices;
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

        if (alreadyPickedChoices != null) {
            holder.choiceField.setOnClickListener(v -> {
                if (selected != null)
                    selected.setChecked(false);
                holder.choiceField.setChecked(true);
                selected = holder.choiceField;
                pickedIds.add((int) getItemId(position));
            });
        }
//        } else {
//            for (BallotChoice bc : alreadyPickedChoices) {
//                if (bc.getChoice().getId() == entityList.get(position).getId()) {
//                    holder.choiceField.setChecked(true);
//                    break;
//                }
//            }
//            holder.choiceField.setEnabled(false);
//        }

        return convertView;
    }


}
