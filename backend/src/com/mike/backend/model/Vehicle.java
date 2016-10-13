package com.mike.backend.model;

import com.mike.backend.db.Node;
import com.mike.util.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 10/12/2016.

 always on a guide, some fraction of the distance between
 guide.from and guide.to, in the range [0..1]

 heading, velocity and length
 length is heading + 180 from the location
 */
public abstract class Vehicle extends PhysicalObject {

    private Guide guide;
    private double guideDistance;

    private double heading;
    private double velocity; // in the direction it is heading
    private double length;

    /**
     create from database record

     @param rs
            col 1 = id
            col 2 = Guide id
            col 3 = Guide distance
            col 4 = heading
            col 5 = velocity
            col 6 = length

     @throws SQLException
     */
    Vehicle(Node parent, ResultSet rs) throws SQLException {
        super(parent, rs.getLong(1));

        this.guide = Guide.get(rs.getLong(2));
        this.guideDistance = rs.getDouble((3));
        this.heading = rs.getDouble(4);
        this.velocity = rs.getDouble(5);
        this.length = rs.getDouble(6);
   }
}
