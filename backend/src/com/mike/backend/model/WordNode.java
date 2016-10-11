package com.mike.backend.model;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.backend.Constants;
import com.mike.backend.db.Node;
import com.mike.util.NYIError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/12/2016.
 */
public class WordNode extends Node {

    private static final String TAG = WordNode.class.getSimpleName();

    static private List<WordNode> nodes = new ArrayList<>();

    public static long getCount() {
        return nodes.size();
    }

    static public WordNode findWord(String s) {

        for (WordNode n : nodes) {
            if (n.word.equals(s))
                return n;
        }

        return null;
    }

//    static public Node findOrAdd(String s) {
//        Node n;
//        try {
//            n = findWord(s);
//        } catch (IllegalStateException e) {
//            n = new Node(s);
//        }
//
//        return n;
//    }

    static public WordNode findOrAdd(String word, String pos, String gpos) {
        Node n = findWord(word);
        if ((n != null) && (n instanceof WordNode))
            return (WordNode) n;

        String s = pos;
        if (pos.matches("[.!?,:]"))
            s = "PUNCT";

        //        gLRB (38),      //  "("   4       -LRB-         _       .       -LRB-   _       3       punct   _       _
        else if (pos.equals("-LRB-"))
            s = "gLRB";

        //        gRRB (39),      //  ")"   11      -RRB-         _       .       -RRB-   _       3       punct   _       _
        else if (pos.equals("-RRB-"))
            s = "gRRB";

        Constants.PoS p;
        try {
            p = Constants.PoS.valueOf(s);
            GrammarRecognizerClause.GPoS gp = GrammarRecognizerClause.GPoS.valueOf(gpos);
            return new WordNode(word, p, gp);
        }
        catch (IllegalArgumentException e) {
            throw new NYIError(String.format("Unknown PoS or GPoS enum value %s/%s", s, gpos));
//            return null;
        }
    }


    private String word;
    private Constants.PoS pos;
    private GrammarRecognizerClause.GPoS gpos;

    public WordNode(String word, Constants.PoS pos, GrammarRecognizerClause.GPoS gpos) {
        this.word = word;
        this.pos = pos;
        this.gpos = gpos;

        nodes.add(this);
    }

//    public List<MessageArc> getIncoming(MessageIterator id) {
//        List<MessageArc> v = new ArrayList<>();
//        for (Arc a : incoming) {
//            assert (a instanceof MessageArc);
//            MessageArc ma = (MessageArc) a;
//            if (ma.getID().msgID == id.msgID)
//                if (ma.getID().wordIndex == id.wordIndex - 1)
//                    v.add(ma);
//        }
//        return v;
//    }
//
//    public List<MessageArc> getOutgoing(MessageIterator id) {
//        List<MessageArc> v = new ArrayList<>();
//        for (Arc a : outgoing) {
//            assert (a instanceof MessageArc);
//            MessageArc ma = (MessageArc) a;
//            if (ma.getID().msgID == id.msgID)
//                if (ma.getID().wordIndex == id.wordIndex + 1)
//                    v.add(ma);
//        }
//        return v;
//    }

    public String getWord() {
        return word;
    }
    public Constants.PoS getPoS() { return pos; }
    public GrammarRecognizerClause.GPoS getGPoS() { return gpos; }

    @Override
    public String dump() { return String.format("%s %s", TAG, toString()); }

    @Override
    public String toString() { return String.format("(%s, %s, %s)", this.word, this.pos, this.gpos); }
}
