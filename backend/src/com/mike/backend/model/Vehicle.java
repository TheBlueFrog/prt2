package com.mike.backend.model;

import com.mike.util.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 10/12/2016.

 location, heading, velocity and length
 length is heading + 180 from the location
 */
public abstract class Vehicle extends PhysicalPoint {

    private double heading;
    private double velocity; // in the direction it is heading
    private double length;

    /**
     create from database record

     @param rs
            col 1 = id
            col 2 = x
            col 3 = y
            col 4 = heading
            col 5 = velocity
            col 6 = length

     @throws SQLException
     */
    Vehicle(ResultSet rs) throws SQLException {
        super(rs.getLong(1),
              new Location(rs.getDouble(2), rs.getDouble((3))));

        this.heading = rs.getDouble(4);
        this.velocity = rs.getDouble(5);
        this.length = rs.getDouble(6);
   }
}
