package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Choice;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoicesAdapter extends CustomAdapter<Choice> {
    private final List<CheckBox> selected = new ArrayList<>();
    private final int maxChoices;
    private int count = 0;

    public MultipleChoicesAdapter(Context context, List<Choice> entityList, int maxChoices) {
        super(context, entityList);
        this.maxChoices = maxChoices;

    }

    public static class ViewHolder {
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
        holder.choiceField.setText(entityList.get(position).getNames().toString());

        holder.choiceField.setOnClickListener(v -> {
            if (selected.contains(holder.choiceField)) {
                pickedIds.remove(selected.indexOf(holder.choiceField));
                selected.remove(holder.choiceField);
                holder.choiceField.setChecked(false);
                count--;
            } else {
                if (count < maxChoices) {
                    count ++;
                } else {
                    selected.get(0).setChecked(false);
                    pickedIds.remove(0);
                    selected.remove(0);
                }
                pickedIds.add((int)getItemId(position));
                selected.add(holder.choiceField);
                holder.choiceField.setChecked(true);
            }
        });

        return convertView;
    }
}
