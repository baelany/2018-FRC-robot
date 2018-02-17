/**
 * A Mission that will load a MissionScript file every time it is run.
 * The MissionScriptMission commands are set by the MissionScript file
 * and updated every time its reset function is called.
 */
package com.teamcautionrobotics.autonomous;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

import com.teamcautionrobotics.autonomous.commands.CommandFactory;

public class MissionScriptMission extends Mission {
    Path missionScriptFile;

    CommandFactory commandFactory;

    public MissionScriptMission(String name, Path missionScriptFile,
            CommandFactory commandFactory, boolean enableControls) {
        super(name, enableControls);
        this.missionScriptFile = missionScriptFile;
        this.commandFactory = commandFactory;
    }


    public MissionScriptMission(String name, Path missionScriptFile, CommandFactory commandFactory) {
        this(name, missionScriptFile, commandFactory, false);
    }

    @Override
    public synchronized void reset() {
        step = commands.size();
        Mission parsedMission = null;
        try {
            parsedMission = MissionScript.parseMission(this.getName(),
                    Files.readAllLines(missionScriptFile), commandFactory);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        if (parsedMission != null) {
            commands = parsedMission.getCommands();
        } else {
            System.err.println("MissionScriptMission: MissionScript parser returned null Mission!");
        }

        super.reset();
    }

    @Override
    public synchronized boolean run() {
        return super.run();
    }
}
