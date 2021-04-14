package fr.univtln.mapare.controllers;

import java.util.ArrayList;
import java.util.List;

public class Controller<E> {
    private List<E> list = new ArrayList<>();

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public void listAdd(E e) {
        if (!list.contains(e))
            list.add(e);
    }
}
