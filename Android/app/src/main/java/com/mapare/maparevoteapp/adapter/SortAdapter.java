package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.mapare.maparevoteapp.R;

import java.util.List;

/**
 * The type Sort adapter.
 */
public class SortAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> sortlist;
    private RadioButton selected;
    private final String tempSelected;

    /**
     * Instantiates a new Sort adapter.
     *
     * @param context      the context
     * @param sortlist    the sort list
     * @param tempSelected the temp selected
     */
    public SortAdapter(Context context, List<String> sortlist, String tempSelected) {
        this.inflater = LayoutInflater.from(context);
        this.sortlist = sortlist;
        this.tempSelected = tempSelected;

    }

    /**
     * The type View holder.
     */
    public static class ViewHolder {
        /**
         * The Sort field.
         */
        RadioButton sortField;
    }

    @Override
    public int getCount() {
        return sortlist.size();
    }

    @Override
    public Object getItem(int position) {
        return sortlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sorting_list, null); // null needed here
            holder = new ViewHolder();
            holder.sortField = convertView.findViewById(R.id.sort_radioButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sortField.setText(sortlist.get(position));
        if (selected == null && holder.sortField.getText().toString().equals(tempSelected)) {
            holder.sortField.setChecked(true);
            selected = holder.sortField;
        }


        holder.sortField.setOnClickListener(v -> {
            selected.setChecked(false);
            holder.sortField.setChecked(true);
            selected = holder.sortField;

        });
        return convertView;
    }

    /**
     * Gets sort picked.
     *
     * @return the sort picked
     */
    public String getSortPicked() {
        return selected.getText().toString();
    }
}
