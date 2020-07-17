package com.fibbery.cli.command;

import java.util.List;

public interface ICommand {

    /**
     * 执行命令的标志符
     * @return
     */
    String getIdentifier();

    /**
     * 执行对应命令
     * @param args
     */
    void execute(String[] args);

    /**
     * 打印帮助文档，以及命令描述
     */
    void printUsage();
}
