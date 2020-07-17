package com.fibbery.cli.utils;

import com.blinkfox.minitable.MiniTable;

public class TableUtils {

    public static void printTable(Object[] headers, Object[][] data) {
        MiniTable table = new MiniTable();
        table.addHeaders(headers);
        for (Object[] datum : data) {
            table.addDatas(datum);
        }
        System.out.println(table.render());
    }
}
