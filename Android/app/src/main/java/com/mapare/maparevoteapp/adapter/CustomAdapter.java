package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.mapare.maparevoteapp.model.EntityWithId;

import java.util.HashMap;
import java.util.List;

public abstract class CustomAdapter<E extends EntityWithId> extends BaseAdapter {
    protected final LayoutInflater inflater;
    protected List<E> entityList;
    protected HashMap<Integer, Integer> pickedIds = new HashMap<>();

    public CustomAdapter(Context context, List<E> entityList) {
        this.inflater = LayoutInflater.from(context);
        this.entityList = entityList;
    }

    @Override
    public int getCount() {
        return entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entityList.get(position).getId();
    }

    public HashMap<Integer, Integer>  getPickedOnes() {
        return pickedIds;
    }

}
