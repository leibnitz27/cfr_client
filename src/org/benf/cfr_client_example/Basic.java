package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;
import java.util.Arrays;

public class Basic {
    public static void main(String ... args) {
        CfrDriver driver = new CfrDriver.Builder().build();
        driver.analyse(Arrays.asList(args));
    }
}
