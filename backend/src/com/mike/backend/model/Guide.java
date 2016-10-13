package com.mike.backend.model;

import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Created by mike on 10/12/2016.
 A guide is a virtual rail from one PhysicalPoint to another
 */
public class Guide extends PhysicalObject {

    static private Map<Long, Guide> knownGuides = new HashMap<>();

    static public Map<Long, Guide> getKnownGuides() {
        return knownGuides;
    }

    public static Guide get(long id) {
        return knownGuides.get(id);
    }

    @Override
    public String getTag() {
        return Guide.class.getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("{%s: from %s to %s}", getTag(), from.toString(), to.toString());
    }

    private PhysicalPoint from;
    private PhysicalPoint to;
//    private List<Guide> connected = new ArrayList<>();

    Guide(Node parent, ResultSet rs) throws SQLException {
        super(parent, rs.getLong(1));

        this.from = PhysicalPoint.get(rs.getLong(2));
        this.from.add(this);

        this.to = PhysicalPoint.get(rs.getLong(3));
        this.to.add(this);

        knownGuides.put(id, this);
    }

}
