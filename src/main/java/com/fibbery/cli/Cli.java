package com.fibbery.cli;

import com.beust.jcommander.JCommander;
import com.fibbery.cli.args.CommandArgs;
import com.fibbery.cli.args.MainArgs;
import com.fibbery.cli.command.AbstractCommand;
import com.fibbery.cli.command.CommandFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.util.Scanner;

public class Cli {


    public static void main(String[] args) {
        MainArgs mainArgs = new MainArgs();
        JCommander commander = JCommander.newBuilder().build();
        commander.addObject(mainArgs);
        commander.parse(args);

        CommandFactory.init(mainArgs.getNameServerAddresses());




        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.print("\t>\t");
            CommandArgs commandArgs = new CommandArgs();
            JCommander.newBuilder().addObject(commandArgs).build().parse(scanner.next());
            String cmd = commandArgs.getArgs().get(0);
            AbstractCommand command = CommandFactory.getCommand(cmd);
            if (command == null) {
                System.out.println("\t|\tcommand [" + cmd + "] not exist!!!");
            } else {
                command.execute(commandArgs.getArgs().subList(0, commandArgs.getArgs().size()));
            }
        }
    }

}
