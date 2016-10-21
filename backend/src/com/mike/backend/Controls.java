package com.mike.backend;

import com.mike.backend.agents.MyClock;
import com.mike.backend.model.Vehicle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 * Created by mike on 2/14/2016.
 */
public class Controls extends JPanel implements ActionListener {
    private JToggleButton showVehicleLabel;
    protected JButton runButton, stepButton, pauseButton;
        private JFrame mFrame;

        public Controls() {

            // Create and set up the controls
            runButton = new JButton("Run");
            runButton.setVerticalTextPosition(AbstractButton.CENTER);
            runButton.setHorizontalTextPosition(AbstractButton.LEADING); // aka LEFT, for
            // left-to-right
            // locales
            runButton.setMnemonic(KeyEvent.VK_D);
            runButton.setActionCommand("run");
            runButton.setEnabled( ! Main.getRunning());

            stepButton = new JButton("Step");
            stepButton.setVerticalTextPosition(AbstractButton.BOTTOM);
            stepButton.setHorizontalTextPosition(AbstractButton.CENTER);
            stepButton.setMnemonic(KeyEvent.VK_M);
            stepButton.setActionCommand("step");
            stepButton.setEnabled( ! Main.getRunning());

            pauseButton = new JButton("Pause");
            // Use the default text position of CENTER, TRAILING (RIGHT).
            pauseButton.setMnemonic(KeyEvent.VK_E);
            pauseButton.setActionCommand("pause");
            pauseButton.setEnabled(Main.getRunning());

            showVehicleLabel = new JToggleButton("Vehicle labels");
            //showVehicleLabel.setMnemonic(KeyEvent.VK_E);
            showVehicleLabel.setActionCommand("show-vehicle-labels");
            showVehicleLabel.setEnabled(true);
            showVehicleLabel.setSelected(Vehicle.getShowLabels());

            // Listen for actions on the buttons
            runButton.addActionListener(this);
            stepButton.addActionListener(this);
            pauseButton.addActionListener(this);
            showVehicleLabel.addActionListener(this);

            runButton.setToolTipText("Click this button to disable the middle button.");
            stepButton.setToolTipText("This middle button repaints");
            pauseButton.setToolTipText("Click this button to enable the middle button.");

            // Add Components to this container, using the default FlowLayout.
            add(runButton);
            add(stepButton);
            add(pauseButton);
            add(showVehicleLabel);

            mFrame = new JFrame("Controls");
            mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            setOpaque(true); // content panes must be opaque
            mFrame.setContentPane(this);

            // Display the window.
            mFrame.pack();
            mFrame.setVisible(true);

//            int delay = 50; // milliseconds
//            ActionListener taskPerformer = new ActionListener() {
//                public void actionPerformed(ActionEvent evt) {
//
//                    if (running) {
//
//                        Main.stepSimulation();
//                        Main.repaint();
//                    }
//                }
//            };
//
//            // stepping happens at the Agent level driven
//            // by the Clock agent
//
//            new Timer(delay, taskPerformer).start();
        }

        public void actionPerformed(ActionEvent e) {
            if ("pause".equals(e.getActionCommand())) {
                runButton.setEnabled(true);
                stepButton.setEnabled(true);
                pauseButton.setEnabled(false);
                Main.setRunning(false);
            }
            else if ("run".equals(e.getActionCommand())) {
                runButton.setEnabled(false);
                stepButton.setEnabled(false);
                pauseButton.setEnabled(true);
                Main.setRunning(true);
            }
            else if ("show-vehicle-labels".equals(e.getActionCommand())) {
                Main.setShowVehicleLables(showVehicleLabel.isSelected());
            }
            else if ("step".equals(e.getActionCommand())) {
//                Main.simulation.step();
                MyClock.singleStep();
            }
        }

    }
