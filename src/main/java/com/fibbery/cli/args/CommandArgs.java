package com.fibbery.cli.args;

import com.beust.jcommander.Parameter;
import lombok.Data;

import java.util.List;

@Data
public class CommandArgs {

    @Parameter(description = "command args",required = true)
    private List<String> args;
}
