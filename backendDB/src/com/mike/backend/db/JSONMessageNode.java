package com.mike.backend.db;

import com.mike.backend.db.DBMessage;
import com.mike.backend.db.AbstractMessageNode;
import org.json.simple.JSONObject;

/**
 * Created by mike on 9/7/2016.
 */
public class JSONMessageNode extends AbstractMessageNode {


    private JSONObject json;

    public JSONMessageNode(DBMessage m) {
        super(m);

        this.json = m.getJSON();
    }

    @Override
    public String dump() {
        assert false; // nyi
        return null;
    }

    public JSONObject getJSON() {
        return json;
    }
}
