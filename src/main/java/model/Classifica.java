package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Classifica
{
    private ArrayList<Team> classifica;

    public Classifica()
    {
        this.classifica = new ArrayList<>();
    }

    public void updateClassifica(ArrayList<Team> teams)
    {
        classifica.clear();

        for(Team t : teams)
            classifica.add(t);
    }

    public void calcolaClassifica()
    {
        classifica.sort(Comparator.comparingInt(Team::getVoto).reversed());
    }

    public ArrayList<Team> getClassifica()
    {
        return classifica;
    }
}
