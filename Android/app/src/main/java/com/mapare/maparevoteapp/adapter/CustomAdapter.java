package com.mapare.maparevoteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.mapare.maparevoteapp.model.EntityWithId;
import com.mapare.maparevoteapp.model.entity_to_receive.Ballot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Custom adapter.
 *
 * @param <E> the type parameter
 */
public abstract class CustomAdapter<E extends EntityWithId> extends BaseAdapter {
    protected Ballot ballot;
    protected Boolean anonymous;
    /**
     * The Inflater.
     */
    protected final LayoutInflater inflater;
    /**
     * The Entity list.
     */
    protected List<E> entityList;
    /**
     * The Picked ids.
     */
    protected HashMap<Integer, Integer> pickedIds = new HashMap<>();

    /**
     * Instantiates a new Custom adapter.
     *
     * @param context    the context
     * @param entityList the entity list
     */
    protected CustomAdapter(Context context, List<E> entityList) {
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


    /**
     * Gets picked ones.
     *
     * @return the picked ones
     */
    public Map<Integer, Integer> getPickedOnes() {
        return pickedIds;
    }

}
