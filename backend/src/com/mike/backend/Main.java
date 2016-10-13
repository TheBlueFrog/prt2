package com.mike.backend;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */


/*
	cd to root of project
	java -cp "sqlite-jdbc-3.8.11.2.jar;backend\out\production\backend" com.mike.backend.Main

	java -cp "out" -jar out\backend.jar com.mike.Main
  */


import com.mike.agents.AgentInfo;
import com.mike.agents.Framework;
import com.mike.backend.agents.*;
import com.mike.backend.db.DB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
	private static Framework mFramework;
	private static boolean animation = true;

	public static void main(String[] args)
	{
		// get the DB open

		try {
			new DB(Constants.DBfname);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		mFramework = new Framework(mAgents);
	}


	// all the agents that do the actual evaluation

	private static List<AgentInfo> mAgents = new ArrayList<>();
	static
	{
		mAgents.add(new AgentInfo(MyClock.class, 1));
		mAgents.add(new AgentInfo(NetworkLoader.class, 1));
	};

	public static boolean isAnimation() {
		return animation;
	}


	// convert map coords (Lat, Lon) into screen display coords

	public static double deg2PixelX(double x) {
		return 0;
	}

	public static double deg2PixelY(double y) {
		return 0;
	}
}
