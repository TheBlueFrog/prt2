package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mike on 9/6/2016.
 */
public abstract class AbstractRecognizer {

    protected List<AbstractRecognizerTarget> targets = new ArrayList<>();

    protected Map<String, String> variables = new HashMap<>();

    public AbstractRecognizer(String[] vars) {
        for (String s : vars)
            variables.put(s, null);
    }

    public void add(AbstractRecognizerTarget target) { this.targets.add(target); }

    abstract public void matches(AbstractMessageNode m, RecognizerResolver resolver);
}
