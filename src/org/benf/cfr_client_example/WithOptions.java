package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;

import java.util.Collections;
import java.util.HashMap;

public class WithOptions {
    public static void main(String ... args) {
        var options = new HashMap<String,String>();
        // I want to see what switch looks like if we don't resugar it.
        options.put("decodestringswitch", "false");
        var driver = new CfrDriver.Builder().withOptions(options).build();
        driver.analyse(Collections.singletonList("out/production/cfr_client/org/benf/cfr_client_example/decompile_me/StringSwitch.class"));
    }
}
