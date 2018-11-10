package org.benf.cfr_client_example.decompile_me;

public class StringSwitch {
    public static String get(String in) {
        switch (in) {
            case "THIS":
                return "Little";
            case "LITTLE":
                return "Piggy";
            case "PIGGY":
                return "Went";
            default:
                return "WEE WEE WEE";
        }
    }
}
