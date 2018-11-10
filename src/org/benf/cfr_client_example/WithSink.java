package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;

import java.util.*;

public class WithSink {
public static void main(String ... args) {
    OutputSinkFactory mySink = new OutputSinkFactory() {
        @Override
        public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
            // I only understand how to sink strings, regardless of what you have to give me.
            return Collections.singletonList(SinkClass.STRING);
        }

        @Override
        public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
            return sinkType == SinkType.JAVA ? System.out::println : ignore -> {};
        }
    };

    CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).build();
    driver.analyse(Collections.singletonList("out/production/cfr_client/org/benf/cfr_client_example/decompile_me/SpyStuff.class"));
}
}
