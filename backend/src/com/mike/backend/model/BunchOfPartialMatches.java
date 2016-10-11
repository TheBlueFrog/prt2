package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mike on 8/10/2016.
 */
public class BunchOfPartialMatches {
    private List<MatchedRecognizer> data = new ArrayList<>();
    private Map<String, String> known = new HashMap<>();
    private List<String> missing = new ArrayList<>();
    private CompletionCB completionCB;


    public interface CompletionCB {
        public void onComplete(MatchedRecognizer bunchOfPartials);
        public void onPartial(MatchedRecognizer bunchOfPartials, Map<String, String> known, List<String> missing);
    }
    public BunchOfPartialMatches(AbstractMessageNode m, AbstractRecognizerTarget t, CompletionCB cb) {
        this.completionCB = cb;
        MatchedRecognizer mr = new MatchedRecognizer(m, t.copy());
        data.add(mr);
        aggregate(mr);
    }

    public boolean isComplete() {
        return missing.size() == 0;
    }

    public void add(AbstractMessageNode m, AbstractRecognizerTarget t) {
        add (new MatchedRecognizer(m, t.copy()));
    }

    public void add(MatchedRecognizer d) {
        data.add(d);
        aggregate(d);
    }

    private void aggregate(MatchedRecognizer data) {
        // aggregate known variables, later occurrences of a variable overwrite
        // earlier values
        known.clear();
        for (MatchedRecognizer d : this.data) {
            Map<String, String> m = d.getMatchedVars();
            for (String s : m.keySet())
                known.put(s, m.get(s));
        }

        // aggregate missing, but only once for each tag
        missing.clear();
        for (MatchedRecognizer d : this.data) {
            List<String> m = d.getUnmatchedVars();
            for (String s : m)
                if ( ! known.containsKey(s))
                    missing.add(s);
        }

        if (isComplete()) {
            data.setKnown(known);
            completionCB.onComplete(data);
        }
        else
            completionCB.onPartial(data, known, missing);
    }

}
