package fr.univtln.mapare.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Controller<E> {
    private Map<Integer, E> map = new HashMap<>();

    public Collection<E> getList() {
        return map.values();
    }

    public Map<Integer, E> getMap() {
        return map;
    }

    public void setMap(Map<Integer, E> map) {
        this.map = map;
    }

    public void mapAdd(int id, E e) {
        map.put(id, e);
    }

    public Boolean mapContainsKey(int id) {
        return map.containsKey(id);
    }

    public E mapGet(int id) {
        return map.get(id);
    }
}
