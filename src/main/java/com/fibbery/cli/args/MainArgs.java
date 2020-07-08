package com.fibbery.cli.args;

import com.beust.jcommander.Parameter;
import com.fibbery.cli.validator.NameServerAddressValidator;
import lombok.Data;

@Data
public class MainArgs {

    @Parameter(names = "-h", description = "the Nameserver addresses list, split by ;", validateValueWith = NameServerAddressValidator.class, required = true)
    private String nameServerAddresses;
}
