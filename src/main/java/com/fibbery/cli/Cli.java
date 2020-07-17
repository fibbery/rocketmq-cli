package com.fibbery.cli;

import com.beust.jcommander.JCommander;
import com.fibbery.cli.args.MainArgs;
import com.fibbery.cli.command.ICommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.MQAdminExt;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.*;

public class Cli {

    public static final HashMap<String, ICommand> COMMANDS = new HashMap<>();

    public static MQAdminExt MQ_ADMIN;

    private static final long TIME_OUT = 3000;

    public static void main(String[] args) throws IOException {
        MainArgs mainArgs = new MainArgs();
        JCommander commander = JCommander.newBuilder().build();
        commander.addObject(mainArgs);
        commander.parse(args);

        startMqAdmin(mainArgs);
        List<Completer> completers = initCommands();


        Terminal terminal = TerminalBuilder.terminal();
        LineReader lineReader = LineReaderBuilder.builder().completer(new AggregateCompleter(completers)).terminal(terminal).build();

        String line = "";
        while (true) {
            line = lineReader.readLine("rocket-cli > ");
            if (StringUtils.isEmpty(line.trim())) {
                continue;
            }
            String[] cmdArgs = line.split(" ");
            ICommand cmd = COMMANDS.get(cmdArgs[0]);
            if ( cmd == null) {
                System.out.println("please enter conrrect command!!!");
                continue;
            }
            cmd.execute(cmdArgs.length < 2 ? null : Arrays.copyOfRange(cmdArgs, 1, cmdArgs.length));

        }

    }

    /**
     * init command map and generate command completer
     */
    private static List<Completer> initCommands() {
        List<Completer> completers = new ArrayList<>();
        ServiceLoader<ICommand> loader = ServiceLoader.load(ICommand.class);
        for (ICommand cmd : loader) {
            COMMANDS.put(cmd.getIdentifier(), cmd);
            completers.add(new ArgumentCompleter(new StringsCompleter(cmd.getIdentifier()), NullCompleter.INSTANCE));
        }
        return completers;
    }

    /**
     * start mq admin
     *
     * @param mainArgs
     */
    private static void startMqAdmin(MainArgs mainArgs) {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt(TIME_OUT);
        mqAdminExt.setNamesrvAddr(mainArgs.getNameServerAddresses());
        MQ_ADMIN = mqAdminExt;
        try {
            mqAdminExt.start();
        } catch (MQClientException e) {
            System.out.println("start mq admin error");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
