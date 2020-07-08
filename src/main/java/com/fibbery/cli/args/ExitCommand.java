package com.fibbery.cli.args;

import com.fibbery.cli.command.AbstractCommand;

import java.util.List;

public class ExitCommand extends AbstractCommand {

    @Override
    public void execute(List<String> args) {
        System.exit(-1);
    }

    @Override
    public String getIdentifier() {
        return "exit";
    }
}
