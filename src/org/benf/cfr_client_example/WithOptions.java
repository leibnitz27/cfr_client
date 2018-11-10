package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WithOptions {
public static void main(String ... args) {
    Map<String, String> options = new HashMap<>();
    options.put("decodestringswitch", "false");
    CfrDriver driver = new CfrDriver.Builder().withOptions(options).build();
    driver.analyse(Collections.singletonList("out/production/cfr_client/org/benf/cfr_client_example/decompile_me/StringSwitch.class"));
}
}
