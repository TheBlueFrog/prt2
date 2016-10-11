package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mike on 9/6/2016.
 */
public abstract class AbstractRecognizerTarget {
    protected String verb;
    protected Map<String, String> variables;
    protected AbstractMessageNode message;

    public AbstractRecognizerTarget(String verb, List<String> variables) {
        this.verb = verb;

        this.variables = new HashMap<String, String>();
        if (variables != null)
            for (String s : variables)
                this.variables.put(s, null);
    }

    public abstract boolean matches(AbstractMessageNode m);

    protected boolean matchLiteral(String a, String b) {
        return a.toLowerCase().equals(b.toLowerCase());
    }

    protected void clearMatches() {
        for (String s : variables.keySet())
            variables.put(s, null);
    }

    public int getNumMatches() {
        int i = 0;
        for (String s : variables.keySet())
            if (variables.get(s) != null)
                i++;
        return i;
    }

    public Map<String, String> getMatchedVars() {
        Map<String, String> v = new HashMap<>();
        for (String s : variables.keySet())
            if (variables.get(s) != null)
                v.put(s, variables.get(s));
        return v;
    }

    public List<String> getUnmatchedVars() {
        List<String> v = new ArrayList<>();
        for (String s : variables.keySet())
            if (variables.get(s) == null)
                v.add(s);
        return v;
    }

    public abstract AbstractRecognizerTarget copy();
}
