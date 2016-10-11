package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 8/3/2016.
 *
 * @TODO // FIXME: 8/12/2016  unclear if targets should declare their own
 * variables or the GrammarRecognizer should, at this point it's a bit confused
 */
public class GrammarRecognizerTarget extends AbstractRecognizerTarget {

    static public interface nodef {
        public boolean f(int nodeIndex);
    }

    private List<GrammarRecognizerClause> clauses;
    private GrammarRecognizerClause curClause;

    public GrammarRecognizerTarget(String verb,
                                   String[] variables,
                                   List<GrammarRecognizerClause> clauses) {
        super(verb, Arrays.asList(variables));
        init(clauses);
    }

    private void init (List<GrammarRecognizerClause> clauses) {
        this.clauses = clauses;
        this.curClause = null;
    }

    // everything in this class is static data, except the variable map,
    // the map contain the recognizer targets and any captured variable
    // values, make sure that gets copied
    public GrammarRecognizerTarget(GrammarRecognizerTarget r) {
        super(r.verb, null);

        init (r.clauses);

        this.variables = new HashMap<String, String>(r.variables);
//        for (String s : r.variables.keySet())
//            this.variables.put(s, r.variables.get(s));
    }

    /**
     * @param amn
     * @return true if any part of Message matches this target
     */
    @Override
    public boolean matches(AbstractMessageNode amn) {

        this.message = amn;
        GrammarMessageNode m = (GrammarMessageNode) amn;

        clearMatches();

        boolean b = false;
        if (matchLiteral(this.verb, m.getWord(m.getRoot()).getWord())) {
            // look for a match for each of the clauses, the order
            // is not important, e.g. "I want cherries on Fridy" or "on Friday I want cherries"
            for (GrammarRecognizerClause c : clauses) {
                if (match(c, m.getRoot()))
                    b = true;
            }
        }

        if (!b)
            clearMatches();

        return b;
    }

    @Override
    public AbstractRecognizerTarget copy() {
        return new GrammarRecognizerTarget(this);
    }

    private boolean match(GrammarRecognizerClause clause, int wordIndex) {
        GrammarMessageNode m = (GrammarMessageNode) message;

        // check each child of parent wordIndex for a match on the clause
        if (m.parentChild.get(wordIndex) != null) {
            for (int i : m.parentChild.get(wordIndex)) {
                WordNode w = m.getWord(i);
                if (clause.getGPoS().equals(w.getGPoS())) {

                    // have a match on GPoS

                    if (clause.getVoV().startsWith("<")) {
                        // it's a variable capture the text
                        this.variables.put(clause.getVoV(), w.getWord());
                    }
                    else if ( ! matchLiteral(clause.getVoV(), w.getWord())) {
                        // it's a literal that doesn't match
                        // fail the match, exit early
                        return false;
                    }

                    // do all subclauses with children of i
                    for (GrammarRecognizerClause c : clause.getSubclauses()) {
                        if (!match(c, i))
                            return false;   // nothing matches this subclause fail match
                    }

                    return true;
                }
            }
        }

        return false;
    }

}
