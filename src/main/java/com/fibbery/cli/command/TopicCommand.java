package com.fibbery.cli.command;


import com.fibbery.cli.consts.CommandConsts;
import com.fibbery.cli.utils.TableUtils;
import lombok.Data;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.admin.TopicOffset;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fibbery.cli.Cli.MQ_ADMIN;

public class TopicCommand implements ICommand {

    private static final String[] HEADERS = new String[]{"BrokerName", "QueueId", "MinOffset", "MaxOffset", "LastUpdateTime"};

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss,SSS");

    @Override
    public String getIdentifier() {
        return CommandConsts.COMMAND_TOPIC;
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("must enter topic name !");
            return;
        }
        String name = args[0];
        try {
            TopicStatsTable topicStatsTable = MQ_ADMIN.examineTopicStats(name);
            if (topicStatsTable == null) {
                System.out.println("topic " + name + " not exit");
                return;
            }
            HashMap<MessageQueue, TopicOffset> offsetTable = topicStatsTable.getOffsetTable();
            String[][] data = parseData(offsetTable);
            TableUtils.printTable(HEADERS, data);
        } catch (InterruptedException | MQBrokerException | MQClientException | RemotingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void printUsage() {

    }

    private String[][] parseData(HashMap<MessageQueue, TopicOffset> offsetTable) {
        return offsetTable.entrySet().stream().map(TopicStatus::assemble).sorted((a, b) -> {
            if (a.getBrokerName().compareTo(b.getBrokerName()) == 0) {
                return a.getQueueId() - b.getQueueId();
            }
            return a.getBrokerName().compareTo(b.getBrokerName());
        }).map(s -> {
            String[] fields = new String[5];
            fields[0] = s.getBrokerName();
            fields[1] = String.valueOf(s.getQueueId());
            fields[2] = String.valueOf(s.getMinOffset());
            fields[3] = String.valueOf(s.getMaxOffset());
            fields[4] = String.valueOf(s.getLastUpdateTime());
            return fields;
        }).collect(Collectors.toList()).toArray(new String[0][0]);
    }


    @Data
    static class TopicStatus {
        private String brokerName;
        private int queueId;
        private long minOffset;
        private long maxOffset;
        private String lastUpdateTime;

        public static TopicStatus assemble(Map.Entry<MessageQueue, TopicOffset> entry) {
            if (entry == null) {
                return null;
            }
            TopicStatus status = new TopicStatus();
            status.setBrokerName(entry.getKey().getBrokerName());
            status.setQueueId(entry.getKey().getQueueId());
            status.setMinOffset(entry.getValue().getMinOffset());
            status.setMaxOffset(entry.getValue().getMaxOffset());
            status.setLastUpdateTime(DATE_FORMAT.format(entry.getValue().getLastUpdateTimestamp()));
            return status;
        }
    }
}

