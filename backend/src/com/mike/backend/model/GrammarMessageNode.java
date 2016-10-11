package com.mike.backend.model;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.backend.Constants;
import com.mike.backend.db.AbstractMessageNode;
import com.mike.backend.db.DBMessage;
import com.mike.backend.db.SearchContext;
import com.mike.util.Log;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by mike on 6/12/2016.
 */
public class GrammarMessageNode extends AbstractMessageNode {

    private static final String TAG = GrammarMessageNode.class.getSimpleName();


    private JSONObject json = null;

    private final List<WordNode> words = new ArrayList<>();
    public final Map<Integer, Integer> childParent = new HashMap<>();
    public final Map<Integer, List<Integer>> parentChild = new HashMap<>();
    public int root = -1;

    public WordNode getWord(int i) {
        return words.get(i);
    }

    /**
     * @param i     index of node to use as root
     * @param s     set of PoS to determine which nodes are matched, for
     *              each matched node all words there and below are collected
     *              Note, the search does not continue into the subtree where
     *              words are collected, doing so would result in duplicates.
     * @return      collected word list
     */
    public List<String> collect(int i, Set<Constants.PoS> s) {
        final Set<Constants.PoS> set = s;
        final List<String> coll = new ArrayList<>();

        walkb(i, new nodefb() {
            @Override
            public boolean f(GrammarMessageNode m, int i) {
                WordNode w = words.get(i);
                if (set.contains(w.getPoS())) {
                    // this matches, collect all the text here and below
                    walk(i, new nodef() {
                        @Override
                        public void f(GrammarMessageNode m, int j) {
                            WordNode ww = words.get(j);
                            if (set.contains(ww.getPoS()))
                                coll.add (ww.getWord());
                        }
                    });
                    return false;
                }
                return true;
            }
        });

        return coll;
    }

    public boolean isJson() {
        return this.json != null;
    }

    static private interface nodeDump {
        public void f(int i, String indent);
    }

    static public interface nodef {
        public void f(GrammarMessageNode m, int i);
    }
    static public interface nodefb {
        public boolean f(GrammarMessageNode m, int i);
    }

    /**
     * construct a GrammarMessageNode from bits in the database
     */
    public GrammarMessageNode(DBMessage dbm) throws SQLException
    {
        super(dbm);

        // assume it's a TensorFlow pared message

        // string processing
        String[] lines = dbm.getParsed().split("\n");
        List<List<String>> raw = new ArrayList<>();//lines.length);

        for (int i = 0; i < lines.length; ++i) {

            String delimiters = "\\s+";
            String[] m = lines[i].split(delimiters);

            raw.add(i, Arrays.asList(m));
        }

        // raw lines looks like this
/*  columnar
0       1       2       3       4       5       6       7

1       I       _       PRON    PRP     _       2       nsubj   _       _
2       have    _       VERB    VBP     _       0       ROOT    _       _
3       2       _       NUM     CD      _       4       num     _       _
4       bales   _       NOUN    NNS     _       2       dobj    _       _
5       of      _       ADP     IN      _       4       prep    _       _
6       hay     _       NOUN    NN      _       5       pobj    _       _
7       .       _       .       .       _       2       punct   _       _
*/
        // for whatever reason they are 1-based, we shift to 0-based

        // we extract the 1 & 4 columns to make WordNodes (or reuse if they
        // already exist

        try {
            for (List<String> r : raw) {
                WordNode w = WordNode.findOrAdd(r.get(1), r.get(4), r.get(7));
                words.add(w);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "oops");
        }

        // for whatever reason they are 1-based, we shift to 0-based

        // build a map of child -> parent
        for (List<String> r : raw) {
            int child = Integer.parseInt(r.get(0)) - 1;
            int parent = Integer.parseInt(r.get(6)) - 1;
            childParent.put(child, parent);
        }

        // build a map of parent -> children
        for (List<String> r : raw) {
            int child = Integer.parseInt(r.get(0)) - 1;
            int parent = Integer.parseInt(r.get(6)) - 1;
            if (!parentChild.containsKey(parent))
                parentChild.put(parent, new ArrayList<Integer>());
            parentChild.get(parent).add(child);
        }

        // and we need to know the root
        for (List<String> r : raw) {
            if (r.get(6).equals("0"))
                root = Integer.parseInt(r.get(0)) - 1;
        }
        assert root != -1;

        add(this);
    }

    @Override
    public String dump() {
        Log.d(TAG, String.format("%s from %s (%s)",
                timeAsString(),
                getUser().getName(),
                getUser().getGuid()));
//        Log.d(TAG, String.format("Root %s", getRoot().getWord()));

        dumpWalk(root, new nodeDump() {
                @Override
                public void f(int i, String indent) {
                    WordNode w = words.get(i);
                    Log.d(TAG, String.format("%s%s %s %s",
                            indent,
                            w.getWord(),
                            w.getPoS().toString(),
                            w.getGPoS().toString()));
                }
            },
            "");
        return "";
    }

    private void dumpWalk(int root, nodeDump nf, String indent) {
        nf.f(root, indent);
        if (parentChild.containsKey(root))
            for (int i : parentChild.get(root)) {
                dumpWalk(i, nf, indent + "  ");
        }
    }

    /**
     * walk parse tree (or part thereorf) unconditionally
     * @param root
     * @param nf    function called on every node
     */
    public void walk(int root, nodef nf) {
        nf.f(this, root);
        if (parentChild.containsKey(root))
            for (int i : parentChild.get(root)) {
                walk(i, nf);
            }
    }

    /**
     * walk parse tree (or part thereorf) conditionally
     * @param root
     * @param nf    function called on each node, return
     *              true to continue walk, false stops
     *              the walk
     */
    public void walkb(int root, nodefb nf) {
        if (nf.f(this, root)) {
            if (parentChild.containsKey(root))
                for (int i : parentChild.get(root)) {
                    walkb(i, nf);
                }
        }
    }

    static public interface searchf {
        public void f(SearchContext sc, WordNode w);
    }


    /**
     * @return index of the root node in the parse tree
     */
    public int getRoot() {
        return root;
    }


}
