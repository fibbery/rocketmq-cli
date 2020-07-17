package com.fibbery.cli.command;

import com.fibbery.cli.consts.CommandConsts;

public class QuitCommand implements ICommand{

    @Override
    public String getIdentifier() {
        return CommandConsts.COMMAND_QUIT;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Good Bye !");
        System.exit(-1);
    }

    @Override
    public void printUsage() {

    }
}
