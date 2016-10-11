package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

/**
 * Created by mike on 8/11/2016.
 */
public class AmazonRecognizer extends AbstractRecognizer {

    public AmazonRecognizer(String[] vars) {
        super(vars);
    }

    @Override
    public void matches(AbstractMessageNode m, RecognizerResolver resolver) {
        for (AbstractRecognizerTarget t : targets) {
            t.matches(m);
        }

        /**
         * given that multiple things may have matched, and partials
         * let somebody specific resolve if it's a partial or a complete match
         */
        resolver.resolve(m, this);
    }
}
