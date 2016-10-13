package com.mike.backend.model;

import com.mike.util.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 Created by mike on 10/12/2016.
 A guide is a virtual rail from one PhysicalPoint to another
 */
public class Guide extends PhysicalObject {

    @Override
    String getTag() {
        return Guide.class.getSimpleName();
    }

    private PhysicalPoint from;
    private PhysicalPoint to;
//    private List<Guide> connected = new ArrayList<>();

    Guide(ResultSet rs) throws SQLException {
        super(rs.getLong(1));

        this.from = PhysicalPoint.get(rs.getLong(2));
        this.from.add(this);

        this.to = PhysicalPoint.get(rs.getLong(3));
        this.to.add(this);
    }

}
