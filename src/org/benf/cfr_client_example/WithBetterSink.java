package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WithBetterSink {
    public static void main(String ... args) {
        var mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                System.out.println("CFR wants to sink " + sinkType + ", and I can choose:");
                collection.forEach(System.out::println);
                if (sinkType == SinkType.JAVA && collection.contains(SinkClass.DECOMPILED)) {
                    // If it's JAVA (reconstructed code), and I'm offered DECOMPILED
                    // (which has sinkClass SinkReturns.Decompiled), I'd prefer that.
                    return Arrays.asList(SinkClass.DECOMPILED, SinkClass.STRING);
                } else {
                    // For anything other than this, I will only sink STRING
                    return Collections.singletonList(SinkClass.STRING);
                }
            }

            Consumer<SinkReturns.Decompiled> dumpDecompiled = d -> {
                System.out.println("Package [" + d.getPackageName() + "] Class [" + d.getClassName() + "]");
                System.out.println(d.getJava());
            };

            @Override
            public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                if (sinkType == SinkType.JAVA && sinkClass == SinkClass.DECOMPILED) {
                    return x -> dumpDecompiled.accept((SinkReturns.Decompiled) x);
                }
                return ignore -> {};
            }
        };

        var driver = new CfrDriver.Builder().withOutputSink(mySink).build();
        driver.analyse(Collections.singletonList("target/classes/org/benf/cfr_client_example/decompile_me/SpyStuff.class"));
    }
}
