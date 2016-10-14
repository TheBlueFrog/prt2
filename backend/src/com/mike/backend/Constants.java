package com.mike.backend;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import com.mike.util.Location;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by mike on 6/9/2016.
 */
public class Constants {

    public static String DBfname = "prt2.db";

    public static double MetersPerMile = 1609.34;

    public static double WindowWidthPixels = 500;
    public static double WindowHeightPixels = 500;

    public static Random random = new Random(12734);

    // convert from Lon/Lat to meters on the map and then
    // from meters to pixels

    public static double deg2PixelX(double x) {
        return (Location.deg2MeterX(x) / Location.MapWidthMeters * WindowWidthPixels) + (WindowWidthPixels / 2.0);
    }
    // screen Y increases going down, map going up
    public static double deg2PixelY(double y) {
        return WindowHeightPixels -
                ((Location.deg2MeterY(y) / Location.MapHeightMeters * WindowHeightPixels)
                        + (WindowHeightPixels / 2.0));
    }

}
