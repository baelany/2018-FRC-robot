package com.teamcautionrobotics.autonomous2018.commands;

import com.teamcautionrobotics.autonomous.Command;
import com.teamcautionrobotics.robot2018.DriveBase;

public class ResetEncoders implements Command {

    private DriveBase driveBase;

    public ResetEncoders(DriveBase driveBase) {
        this.driveBase = driveBase;
    }

    @Override
    public boolean run() {
        driveBase.resetEncoders();
        return true;
    }

    @Override
    public void reset() {

    }

}
