/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package com.teamcautionrobotics.robot2018;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.teamcautionrobotics.autonomous.CommandFactory;
import com.teamcautionrobotics.autonomous.Mission;
import com.teamcautionrobotics.autonomous.MissionScriptMission;
import com.teamcautionrobotics.autonomous.MissionSendable;
import com.teamcautionrobotics.robot2018.AutoEnums.AutoObjective;
import com.teamcautionrobotics.robot2018.AutoEnums.StartingPosition;
import com.teamcautionrobotics.robot2018.Gamepad.Axis;
import com.teamcautionrobotics.robot2018.Gamepad.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */

    static final Path missionScriptPath = Paths.get("/opt/mission.ms");

    DriveBase driveBase;

    EnhancedJoystick driverLeft;
    EnhancedJoystick driverRight;
    Gamepad manipulator;

    Intake intake;
    Climb climb;

    CommandFactory commandFactory;
    MissionScriptMission missionScriptMission;
    MissionSendable missionSendable;

    String fmsData;
    SendableChooser<Mission> missionChooser;
    SendableChooser<StartingPosition> startingPositionChooser;
    SendableChooser<AutoObjective> autoFieldElementChooser;
    HashMap<String, Mission> missions;
    Mission activeMission;

    @Override
    public void robotInit() {
        driveBase = new DriveBase(0, 1, 0, 1, 2, 3);

        driverLeft = new EnhancedJoystick(0, 0.1);
        driverRight = new EnhancedJoystick(1, 0.1);
        manipulator = new Gamepad(2);

        intake = new Intake(2, 3, 4);
        climb = new Climb(5);

        commandFactory = new CommandFactory(driveBase);

        startingPositionChooser = new SendableChooser<>();
        for (StartingPosition i : StartingPosition.values()) {
            startingPositionChooser.addObject(i.name(), i);
        }

        autoFieldElementChooser = new SendableChooser<>();
        for (AutoObjective i : AutoObjective.values()) {
            autoFieldElementChooser.addObject(i.name, i);
        }

        missionChooser = new SendableChooser<>();

        Mission doNothingMission = new Mission("do nothing mission");
        missionChooser.addDefault(doNothingMission.getName(), doNothingMission);
        missions.put(doNothingMission.getName(), doNothingMission);

        missionScriptMission = new MissionScriptMission("Mission Script Mission", missionScriptPath,
                commandFactory);
        missionChooser.addObject("Do not use -- Mission Script", missionScriptMission);
        missions.put(missionScriptMission.getName(), missionScriptMission);

        Mission driveForwardMission = new Mission("drive forward mission",
                commandFactory.moveStraightDistance(0.5, 60, true),
                commandFactory.moveStraight(-0.1, 0.2, false));
        missionChooser.addObject(driveForwardMission.getName(), driveForwardMission);
        missions.put(driveForwardMission.getName(), driveForwardMission);

        Mission centerMissionRightSwitch = new Mission("center mission right switch",
                commandFactory.moveStraightDistance(0.5, 30, true),
                commandFactory.turnInPlace(-0.3, 45),
                commandFactory.moveStraightDistance(0.5, 140, true),
                commandFactory.turnInPlace(0.3, 45),
                commandFactory.moveStraightDistance(0.5, 20, true),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.3, 90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(centerMissionRightSwitch.getName(), centerMissionRightSwitch);
        missions.put(centerMissionRightSwitch.getName(), centerMissionRightSwitch);

        Mission centerMissionLeftSwitch = new Mission("center mission left switch",
                commandFactory.moveStraightDistance(0.5, 30, true),
                commandFactory.turnInPlace(0.3, 60),
                commandFactory.moveStraightDistance(0.5, 160, true),
                commandFactory.turnInPlace(-0.3, 50),
                commandFactory.moveStraightDistance(0.5, 30, true),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(-0.3, 90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(centerMissionLeftSwitch.getName(), centerMissionLeftSwitch);
        missions.put(centerMissionLeftSwitch.getName(), centerMissionLeftSwitch);

        Mission centerMissionRightScale = new Mission("center mission right scale",
                commandFactory.moveStraightDistance(0.5, 30, true),
                commandFactory.turnInPlace(-0.3, 45),
                commandFactory.moveStraightDistance(0.5, 140, true),
                commandFactory.turnInPlace(0.3, 50),
                commandFactory.moveStraightDistance(0.5, 195, true),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.3, 90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(centerMissionRightScale.getName(), centerMissionRightScale);
        missions.put(centerMissionRightScale.getName(), centerMissionRightScale);

        Mission centerMissionLeftScale = new Mission("center mission left scale",
                commandFactory.moveStraightDistance(0.5, 30, true),
                commandFactory.turnInPlace(0.3, 60),
                commandFactory.moveStraightDistance(0.5, 160, true),
                commandFactory.turnInPlace(-0.3, 50),
                commandFactory.moveStraightDistance(0.5, 185, true),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(-0.3, 85), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(centerMissionLeftScale.getName(), centerMissionLeftScale);
        missions.put(centerMissionLeftScale.getName(), centerMissionLeftScale);

        Mission rightMissionSwitch = new Mission("right mission switch",
                commandFactory.moveStraightDistance(0.5, 130, true),
                commandFactory.moveStraight(-0.1, 0.2, false),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.5, -90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(rightMissionSwitch.getName(), rightMissionSwitch);
        missions.put(rightMissionSwitch.getName(), rightMissionSwitch);

        Mission rightMissionScale = new Mission("right mission scale",
                commandFactory.moveStraightDistance(0.5, 260, true),
                commandFactory.moveStraight(-0.1, 0.2, false),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.5, -90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(rightMissionScale.getName(), rightMissionScale);
        missions.put(rightMissionScale.getName(), rightMissionScale);

        Mission leftMissionSwitch = new Mission("left mission switch",
                commandFactory.moveStraightDistance(0.5, 130, true),
                commandFactory.moveStraight(-0.1, 0.2, false),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.5, 90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(leftMissionSwitch.getName(), leftMissionSwitch);
        missions.put(leftMissionSwitch.getName(), leftMissionSwitch);

        Mission leftMissionScale = new Mission("left mission scale",
                commandFactory.moveStraightDistance(0.5, 260, true),
                commandFactory.moveStraight(-0.1, 0.2, false),
                // LIFT THE CUBE!!!!!!!
                commandFactory.turnInPlace(0.5, 90), commandFactory.moveStraight(0.5, 0.3, false)
        // DEPLOY THE CUBE!!!!!!!
        );
        missionChooser.addObject(leftMissionScale.getName(), leftMissionScale);
        missions.put(leftMissionScale.getName(), leftMissionScale);

        SmartDashboard.putData("Autonomous Mode Select", missionChooser);

        missionSendable = new MissionSendable("Teleop Mission", missionChooser::getSelected);
        SmartDashboard.putData(missionSendable);
    }

    @Override
    public void disabledPeriodic() {
        SmartDashboard.putString("selected mission", missionChooser.getSelected().getName());
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString line to get the auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the switch structure
     * below with additional strings. If using the SendableChooser make sure to add them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        fmsData = DriverStation.getInstance().getGameSpecificMessage();
        if (fmsData.length() >= 3) {
            char ourSwitchPosition = fmsData.charAt(0);
            char scalePosition = fmsData.charAt(1);
            char opponentSwitchPosition = fmsData.charAt(2);
            
            if (autoFieldElementChooser.getSelected() == AutoObjective.AUTO_LINE) {
                // drive forward mission
            } else if (autoFieldElementChooser.getSelected() == AutoObjective.SWITCH) {
                if (startingPositionChooser.getSelected() == StartingPosition.CENTER) {
                    if (ourSwitchPosition == 'L') {
                        // mission center left switch
                    } else if (ourSwitchPosition == 'R') {
                        // mission center right switch
                    }
                } else if (startingPositionChooser.getSelected() == StartingPosition.LEFT) {
                    if (ourSwitchPosition == 'L') {
                        // mission left switch
                    } else if (ourSwitchPosition == 'R') {
                        // do nothing
                    }
                } else if (startingPositionChooser.getSelected() == StartingPosition.RIGHT) {
                    if (ourSwitchPosition == 'L') {
                        // do nothing
                    } else if (ourSwitchPosition == 'R') {
                        // mission right switch
                    }
                }
            } else if (autoFieldElementChooser.getSelected() == AutoObjective.SCALE) {
                if (startingPositionChooser.getSelected() == StartingPosition.CENTER) {
                    if (ourSwitchPosition == 'L') {
                        // mission center left scale
                    } else if (ourSwitchPosition == 'R') {
                        // mission center right scale
                    }
                } else if (startingPositionChooser.getSelected() == StartingPosition.LEFT) {
                    if (ourSwitchPosition == 'L') {
                        // mission left scale
                    } else if (ourSwitchPosition == 'R') {
                        // do nothing
                    }
                } else if (startingPositionChooser.getSelected() == StartingPosition.RIGHT) {
                    if (ourSwitchPosition == 'L') {
                        // do nothing
                    } else if (ourSwitchPosition == 'R') {
                        // mission right scale
                    }
                }
            } else if (autoFieldElementChooser.getSelected() == AutoObjective.DO_NOTHING) {
                // do nothing
            }
        } else {
            // do nothing
        }

//        activeMission = missionChooser.getSelected();

        if (activeMission != null) {
            activeMission.reset();
            System.out.println("Mission '" + activeMission.getName() + "' Started");
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        if (activeMission != null) {
            if (activeMission.run()) {
                System.out.println("Mission '" + activeMission.getName() + "' Complete");
                activeMission = null;
            }
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        SmartDashboard.putString("selected mission", missionChooser.getSelected().getName());

        if ((missionSendable.run() && !missionChooser.getSelected().enableControls)
                || driveBase.pidController.isEnabled()) {
            return;
        }

        double forwardCommand = -driverRight.getY();
        double turnCommand = driverLeft.getX();
        driveBase.drive(forwardCommand + turnCommand, forwardCommand - turnCommand);

        if (manipulator.getButton(Button.X)) {
            climb.ascend();
        }

        // Left bumper spins counterclockwise
        if (manipulator.getButton(Button.LEFT_BUMPER)) {
            intake.timedSpin(-0.25, 0.1);
        }

        // Right bumper spins clockwise
        if (manipulator.getButton(Button.RIGHT_BUMPER)) {
            intake.timedSpin(0.25, 0.1);
        }

        intake.move(manipulator.getAxis(Axis.LEFT_Y));
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {}
}
