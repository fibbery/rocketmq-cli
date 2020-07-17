package com.fibbery.cli;

import com.blinkfox.minitable.MiniTable;
import org.junit.Test;

public class CliTest {

    @Test
    public void testTable() {
        MiniTable table = new MiniTable();
        table.addHeaders("header1", "header2");
        table.addDatas("value1", "value2");
    }

}