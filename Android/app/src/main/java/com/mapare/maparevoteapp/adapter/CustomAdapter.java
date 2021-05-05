package com.mapare.maparevoteapp.adapter;

import android.widget.BaseAdapter;

import com.mapare.maparevoteapp.model.EntityWithId;

import java.util.List;

public abstract class CustomAdapter<E extends EntityWithId> extends BaseAdapter {
    protected List<E> entityList;
    protected List<Integer> pickedIds;

    public CustomAdapter(List<E> entityList) {
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

    public List<Integer> getPickedOnes() {
        return pickedIds;
    }
}
