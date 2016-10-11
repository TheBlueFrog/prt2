package com.mike.backend.model;

import java.util.List;

/**
 * Created by mike on 8/3/2016.
 */
public class GrammarRecognizerClause {


    static public enum GPoS
    { ROOT,
        nsubj,
        dobj,
        num,
        aux,
        prep,
        pobj,
        dep,
        cop,
        det,
        poss,
        punct };

    private final GPoS pos;         // part of speech
    private final String vov;       // literal value we must match or variable name
    private final List<GrammarRecognizerClause> subclauses;  // subclases

    public GrammarRecognizerClause(GPoS pos, String vov, List<GrammarRecognizerClause> subclauses) {
        this.pos = pos;
        this.vov = vov;
        this.subclauses = subclauses;
    }

    public GPoS getGPoS() { return pos; }
    public String getVoV() { return vov; }
    public List<GrammarRecognizerClause> getSubclauses() { return subclauses; }
}
