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
import com.mike.backend.db.RootNode;
import com.mike.backend.model.Guide;
import com.mike.backend.model.PhysicalPoint;
import com.mike.backend.model.Vehicle;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main
{
	private static Framework mFramework;
	private static boolean animation = true;

	public static Drawing drawing;
	private static Controls mControls;
//	private static Simulation simulation;

	public static void main(String[] args)
	{
		// get the DB open

		try {
			new DB(Constants.DBfname);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

//		simulation = new Simulation();

		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// create the controls and drawing windows
				mControls = new Controls();
				drawing = new Drawing(1.0);

				mFramework = new Framework(mAgents);
			}
		});
	}


	// all the agents that do the actual evaluation

	private static List<AgentInfo> mAgents = new ArrayList<>();
	static
	{
		mAgents.add(new AgentInfo(MyClock.class, 1));
		mAgents.add(new AgentInfo(Simulation.class, 1));
	};

	public static boolean isAnimation() {
		return animation;
	}



	public static void paint(final Graphics2D g2) {
		Simulation.paint(g2);

//		mFramework.walk(new Framework.agentWalker() {
//			@Override
//			public void f(Agent a) {
//				if (a instanceof PaintableAgent) {
//					((PaintableAgent) a).paint(g2);
//				}
//			}
//		});
	}

	public static void repaint() {
		if (animation)
			Main.drawing.mFrame.repaint();
	}

	public static void stepSimulation() {
//		simulation.step();
	}

	public static void setShowVehicleLables(boolean show) {
		Vehicle.setShowLabels (show);
	}
}
