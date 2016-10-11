package com.mike.backend.model;

import com.mike.backend.db.AbstractMessageNode;

/**
 * Created by mike on 8/11/2016.
 */
public interface RecognizerResolver {
    /**
     * given that multiple things may have matched, and partials
     * let somebody specific resolve if it's a partial or a complete match
     */
    void resolve(AbstractMessageNode m, AbstractRecognizer recognizer);
}
