package com.mike.backend.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 Created by mike on 9/28/2016. */
public class SupplierItem extends ListedItem {

    static public enum Status { Open, Matched, Closed };

    public SupplierItem(UserNode user, String text, long startTime, long endTime, double quantity, String units) {
        super(user,
                Status.Open.ordinal(),
                text,
                startTime, endTime,
                quantity,
                units);
    }

    public SupplierItem(UserNode user, ResultSet rs) throws SQLException {
        super(user, rs);
    }

    @Override
    protected void insert() throws SQLException {
        setItemDataID(DB.getDB().insertSupplierListing(getUser().getID(), this));
    }

    public Status getStatus () {
        return Status.values()[status];
    }

    @Override
    public String getStatusAsString() {
        return Status.values()[status].toString();
    }

}
