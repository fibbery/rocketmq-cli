package com.fibbery.cli.command;

import lombok.Data;
import org.apache.rocketmq.tools.admin.MQAdminExt;

import java.util.List;

@Data
public abstract class AbstractCommand {

    private MQAdminExt mqAdmin;

    /**
     * execute the command
     *
     * @param args
     */
    public abstract void execute(List<String> args);

    /**
     * 获取标志符
     *
     * @return
     */
    public abstract String getIdentifier();

}
