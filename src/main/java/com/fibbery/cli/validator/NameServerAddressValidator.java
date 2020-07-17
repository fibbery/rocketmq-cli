package com.fibbery.cli.validator;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import org.apache.commons.lang3.StringUtils;

public class NameServerAddressValidator implements IValueValidator<String>{


    @Override
    public void validate(String name, String value) throws ParameterException {
        if (StringUtils.isEmpty(value)) {
            throw new ParameterException("NameServer address can't be null!!");
        }

        String[] addresses = value.split(";");
        for (String address : addresses) {
            boolean valid = address.matches("(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)");
            if (!valid) {
                throw new ParameterException("NameServer address is illegal!!");
            }
        }
    }
}
