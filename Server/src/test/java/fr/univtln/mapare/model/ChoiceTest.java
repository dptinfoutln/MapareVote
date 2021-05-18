package fr.univtln.mapare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ChoiceTest {
    @Test
    void testConstructor() {
        ArrayList<String> names = new ArrayList<String>();
        assertEquals("Choice{id=0, names=[], vote=0}", (new Choice(names, new Vote())).toString());
    }

    @Test
    void testAddName() {
        Choice choice = new Choice();
        choice.addName("Name");
        List<String> names = choice.getNames();
        assertEquals(1, names.size());
        assertEquals("Name", names.get(0));
    }

    @Test
    void testAddName2() {
        Choice choice = new Choice();
        choice.addName("Name");
        choice.addName("Name");
        assertEquals(0, choice.getId());
    }

    @Test
    void testToString() {
        Choice choice = new Choice();
        choice.setVote(new Vote());
        assertEquals("Choice{id=0, names=[], vote=0}", choice.toString());
    }
}

