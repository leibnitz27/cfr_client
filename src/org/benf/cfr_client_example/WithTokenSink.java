package org.benf.cfr_client_example;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WithTokenSink {

    /*
     * Adapt behaviour of token stream in to whatever api we prefer.
     */
    private static class TokenStreamAdaptor implements OutputSinkFactory.Sink<SinkReturns.Token> {

        private boolean pendingIndent;
        private int indentLevel = 0;

        @Override
        public void write(SinkReturns.Token t) {
            switch (t.getTokenType()) {
                case INDENT:
                    indentLevel++;
                    break;
                case UNINDENT:
                    indentLevel--;
                    break;
                case NEWLINE:
                    pendingIndent = true;
                    System.out.print("\n");
                    break;
                case IDENTIFIER:
                    if (pendingIndent) flushIndent();
                    boolean defines = t.getFlags().contains(SinkReturns.TokenTypeFlags.DEFINES);
                    System.out.print("/* IDENT " + (defines?"Defined":"") + " */ " + t.getText());
                    break;
                default:
                    // control token that's not handled?  Ignore.
                    if (t.getTokenType().isControl()) break;
                    if (pendingIndent) flushIndent();
                    System.out.print(t.getText());
                    break;
            }

        }

        private void flushIndent() {
            System.out.print("\t".repeat(indentLevel));
            pendingIndent = false;
        }
    }

    public static void main(String ... args) {
        var mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                System.out.println("CFR wants to sink " + sinkType + ", and I can choose:");
                collection.forEach(System.out::println);
                if (sinkType == SinkType.JAVA && collection.contains(SinkClass.TOKEN_STREAM)) {
                    // If it's JAVA (reconstructed code), and I'm offered TOKEN_STREAM
                    // (which has sinkClass SinkReturns.Token), I'd prefer that.
                    return Arrays.asList(SinkClass.TOKEN_STREAM, SinkClass.STRING);
                } else {
                    // For anything other than this, I will only sink STRING
                    return Collections.singletonList(SinkClass.STRING);
                }
            }

            @Override
            public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                if (sinkType == SinkType.JAVA && sinkClass == SinkClass.TOKEN_STREAM) {
                    return (Sink<T>)(new TokenStreamAdaptor());
                }
                return ignore -> {};
            }
        };

        var driver = new CfrDriver.Builder().withOutputSink(mySink).build();
        driver.analyse(Collections.singletonList("target/classes/org/benf/cfr_client_example/decompile_me/SpyStuff.class"));
    }
}
