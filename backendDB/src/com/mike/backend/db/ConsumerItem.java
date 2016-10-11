package com.mike.backend.db;

import com.mike.backend.db.DB;
import com.mike.backend.db.ListedItem;
import com.mike.backend.db.UserNode;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 Created by mike on 9/28/2016.
 */
public class ConsumerItem extends ListedItem {

    static public enum Status { Open, Matched, Closed };


    // this will construct and insert into DB, it is initialized
    // as an open, unsatisfied request
    public ConsumerItem(
            UserNode user,
            String text,
            long startTime, long endTime,
            double quantity,
            String units) {
        super(user, Status.Open.ordinal(), text, startTime, endTime, quantity, units);
    }

    public ConsumerItem(UserNode user, ResultSet rs) throws SQLException {
        super(user, rs);
    }

    @Override
    public String getStatusAsString() {
        return Status.values()[status].toString();
    }

    public Status getStatus () {
        return Status.values()[status];
    }


    @Override
    protected void insert() throws SQLException {
        setItemDataID(DB.getDB().insertConsumerListing(getUser().getID(), this));
    }
}
