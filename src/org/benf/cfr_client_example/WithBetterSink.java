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
    OutputSinkFactory mySink = new OutputSinkFactory() {
        @Override
        public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
            // I only understand how to sink strings, regardless of what you have to give me.
            System.out.println("CFR wants to sink " + sinkType + ", and I can choose:");
            collection.forEach(System.out::println);
            if (sinkType == SinkType.JAVA && collection.contains(SinkClass.DECOMPILED)) {
                return Arrays.asList(SinkClass.DECOMPILED, SinkClass.STRING);
            } else {
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

    CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).build();
    driver.analyse(Collections.singletonList("out/production/cfr_client/org/benf/cfr_client_example/decompile_me/SpyStuff.class"));
}
}
