package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.VoteResult;

import java.util.List;

/**
 * The type Result adapter.
 */
public class ResultAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final List<VoteResult> results;

    /**
     * Instantiates a new Result adapter.
     *
     * @param context the context
     * @param results the results
     */
    public ResultAdapter(Context context, List<VoteResult> results) {
        this.inflater = LayoutInflater.from(context);
        this.results = results;
    }

    @Override
    public int getGroupCount() {
        return results.isEmpty() ? 0 : 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return results.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return results;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return results.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return results.get(childPosition).getChoice().getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * The type Group view holder.
     */
    public static class GroupViewHolder {
        /**
         * The Text.
         */
        TextView text;
        /**
         * The Img.
         */
        ImageView img;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.show_result, null);
            holder = new GroupViewHolder();
            holder.text = convertView.findViewById(R.id.result_show);
            holder.img = convertView.findViewById(R.id.result_arrow);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.text.setText(R.string.show_results);

        if (isExpanded) {
            holder.img.setImageResource(R.drawable.ic_arrow_up);
        } else {
            holder.img.setImageResource(R.drawable.ic_arrow_down);
        }

        return convertView;
    }

    /**
     * The type Child view holder.
     */
    public static class ChildViewHolder {
        /**
         * The Choice field.
         */
        TextView choiceField;
        /**
         * The Result field.
         */
        TextView resultField;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.result_list, null);
            holder = new ChildViewHolder();
            holder.choiceField = convertView.findViewById(R.id.result_choice);
            holder.resultField = convertView.findViewById(R.id.result_value);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.choiceField.setText(results.get(childPosition).getChoice().getNames().get(0));
        String value = results.get(childPosition).getValue()+"";
        holder.resultField.setText(value);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
