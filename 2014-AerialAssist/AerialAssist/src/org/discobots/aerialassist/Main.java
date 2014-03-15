/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.discobots.aerialassist;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.discobots.aerialassist.commands.CommandBase;
import org.discobots.aerialassist.utils.Dashboard;
import org.discobots.aerialassist.commands.Autonomous;
import org.discobots.aerialassist.commands.drive.SetMiniCimUsage;
import org.discobots.aerialassist.commands.drive.SwitchDrive;
import org.discobots.aerialassist.commands.upperbody.ToggleArm;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Main extends IterativeRobot {
    
    Command autonomousCommand;
    int autonMode = 2;
    public static final String AUTONCHOOSER_ = "Autonomous Chooser";
    public static final String AUTONMODE_ = "Autonomous Mode";
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void robotInit() {
        CommandBase.init();
        Dashboard.init();
        SmartDashboard.putNumber(AUTONCHOOSER_, 2);
    }

    public void autonomousInit() {
        autonomousCommand = new Autonomous(autonMode);
        autonomousCommand.start();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Dashboard.update();
    }

    public void teleopInit() {
        if (autonomousCommand != null)
        autonomousCommand.cancel();
        new SwitchDrive(SwitchDrive.MODE_OMNIWHEEL, SwitchDrive.MODE_NULL).start();
        new SetMiniCimUsage(true).start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        long begin=System.currentTimeMillis();
        Scheduler.getInstance().run();
        updateAutonomousSelection();
        Dashboard.update();
        SmartDashboard.putNumber("Main Loop Time", System.currentTimeMillis()-begin);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Scheduler.getInstance().run();
        Dashboard.update();
        LiveWindow.run();
    }
    
    public void disabledPeriodic() {
        Dashboard.update();
        updateAutonomousSelection();
    }
    
    public void disabledInit() {
        System.out.println("Disabled.");
        new ToggleArm(true).start();
    }
    
    public void updateAutonomousSelection() {
        this.autonMode = (int) SmartDashboard.getNumber(AUTONCHOOSER_, 2);
        String autonString = "error";
        SmartDashboard.putString("545454", "545454545454545454545454545454545454");
        switch(autonMode) {
            case 1:
                autonString = "TWO BALL: HIGH HIGH";
                break;
            case 2:
                autonString = "TWO BALL: LOW HIGH";
                break;
            default:
                autonMode = 0;
                autonString = "DO NOTHING";
                break;
        }
        SmartDashboard.putString(AUTONMODE_, autonString);
    }
}
