package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

import java.util.List;
import java.util.Map;

import static com.mike.util.Util.flatten;

/**
 * an instance of this class is passed to this agent after
 * being matched (partially or fully) out of raw input.
 */
public class MatchedRecognizer {
    public AbstractMessageNode message;
    public AbstractRecognizerTarget target = null;
    private Map<String, String> known = null;

    public MatchedRecognizer(AbstractMessageNode messsage) {
        this.message = messsage;
    }

    public MatchedRecognizer(AbstractMessageNode messsage, AbstractRecognizerTarget target) {
        this.message = messsage;
        this.target = target;
    }

    public Map<String, String> getMatchedVars() {
        return target.getMatchedVars();
    }

    public List<String> getUnmatchedVars() {
        return target.getUnmatchedVars();
    }

    @Override
    public String toString() {
        return flatten(known);
    }

    public void setKnown(Map<String, String> known) {
        this.known = known;
    }

    public String getKnown(String var) {
        return known.get(var);
    }

    public Map<String, String> getKnown() {
        return known;
    }
}
