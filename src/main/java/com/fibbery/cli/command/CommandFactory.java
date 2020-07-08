package com.fibbery.cli.command;

import com.fibbery.cli.args.ExitCommand;
import com.fibbery.cli.args.TopicCommand;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandFactory {

    public static final HashMap<String, AbstractCommand> COMMAND_MAP = new HashMap<String, AbstractCommand>();

    private static final AtomicBoolean HAS_INITED = new AtomicBoolean(false);

    public static AbstractCommand getCommand(String name) {
        return COMMAND_MAP.get(name);
    }

    public static void init(String nameServerAddresses) {
        if (!HAS_INITED.compareAndSet(false, true)) {
            return;
        }

        // start mqadmn
        DefaultMQAdminExt mqAdmin = new DefaultMQAdminExt();
        mqAdmin.setNamesrvAddr(nameServerAddresses);
        try {
            mqAdmin.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        TopicCommand topicCommand = new TopicCommand();
        topicCommand.setMqAdmin(mqAdmin);
        COMMAND_MAP.put(topicCommand.getIdentifier(), topicCommand);


        ExitCommand exitCommand = new ExitCommand();
        exitCommand.setMqAdmin(mqAdmin);
        COMMAND_MAP.put(exitCommand.getIdentifier(), exitCommand);
    }
}
