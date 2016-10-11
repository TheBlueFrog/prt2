package com.mike.backend.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 9/28/2016. */
abstract public class ListedItem {

    private UserNode user;
    private long ownerID;
    protected int status;       // we manage to/from DB but that's about it
    private long startTime;
    private long endTime;
    private double quantity;
    private String units;

    private long itemDataID;    // stored in ItemData table
    private String item;

    abstract protected void insert() throws SQLException;

    // at this level status is an int have subclass handle this
    abstract public String getStatusAsString();

    protected void setItemDataID(long id) {
        itemDataID = id;
    }

    public String getText() {
        return item;
    }
    public long getStartTime() {
        return startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public double getQuantity() {
        return quantity;
    }
    public String getUnits() {
        return units;
    }
    public UserNode getUser() {
        return user;
    }
    public String getItem() {
        return item;
    }

    protected ListedItem(UserNode user,
                         int status,
                         String item,
                         long startTime, long endTime,
                         double quantity,
                         String units) {
        this.user = user;
        this.status = status;

        this.startTime = startTime;
        this.endTime = endTime;
        this.quantity = quantity;
        this.units = units;
        this.item = item;

        try {
            insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected ListedItem(UserNode user, ResultSet rs) throws SQLException {
        this.user = user;
//        this.id = rs.getLong(1);
//        this.time = rs.getLong(2);
        ownerID = rs.getLong(3);
        assert ownerID == user.getID();

        status = rs.getInt(4);
        startTime = rs.getLong(5);
        endTime = rs.getLong(6);
        quantity = rs.getDouble(7);
        units = rs.getString(8);

        itemDataID = rs.getLong(9);
        item = DB.getDB().getItemDataText(itemDataID);
    }

    @Override
    public String toString() {
        return String.format("{what: %s, quantity: %f, units: %s, starting: %d, ending %d}",
                item, quantity, units, startTime, endTime);
    }
}
