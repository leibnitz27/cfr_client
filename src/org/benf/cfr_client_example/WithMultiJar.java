package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WithMultiJar {
    public static void main(String ... args) {
        var mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                return Collections.singletonList(SinkClass.DECOMPILED_MULTIVER);
            }

            Consumer<SinkReturns.DecompiledMultiVer> dumpDecompiled = d -> {
                if (d.getRuntimeFrom() > 0) System.out.println("JRE above " + d.getRuntimeFrom());
                System.out.println("Package [" + d.getPackageName() + "] Class [" + d.getClassName() + "]");
                System.out.println(d.getJava());
            };

            @Override
            public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                if (sinkType == SinkType.JAVA && sinkClass == SinkClass.DECOMPILED_MULTIVER) {
                    return x -> dumpDecompiled.accept((SinkReturns.DecompiledMultiVer) x);
                }
                return ignore -> {};
            }
        };

        var driver = new CfrDriver.Builder().withOutputSink(mySink).build();
        driver.analyse(Collections.singletonList("c:\\temp\\multijar2.jar"));
    }

}
