package com.fibbery.cli.args;

import com.fibbery.cli.command.AbstractCommand;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * tpic command
 */
public class TopicCommand extends AbstractCommand {

    @Override
    public void execute(List<String> args) {
        try {
            TopicList topics = this.getMqAdmin().fetchAllTopicList();
            for (String topic : topics.getTopicList()) {
                System.out.println("\t|\t" + topic);
            }
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIdentifier() {
        return "topic";
    }
}
