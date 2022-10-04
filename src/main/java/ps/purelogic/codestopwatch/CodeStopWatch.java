/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ps.purelogic.codestopwatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m.elkhoudary
 */
public class CodeStopWatch {

    private static final ThreadLocal<CodeStopWatch> INSTANCE = new ThreadLocal<>();

    private long startTime;
    private String reason;
    private final List<StopWatchEntry> markedTimes;
    private long lastMark;
    private long stopTime;

    public CodeStopWatch() {
        startTime = 0;
        markedTimes = new LinkedList<>();
    }

    public long benchmark(Runnable runnable, String reason) {
        start(reason);

        mark("Started execution");

        runnable.run();

        mark("Finished execution");

        return stop();
    }

    public void start(String reason) {
        startTime = System.currentTimeMillis();
        lastMark = System.currentTimeMillis();

        this.reason = reason;

        stopTime = 0;
        markedTimes.clear();
    }

    public long stop() {
        stopTime = System.currentTimeMillis();

        return stopTime - startTime;
    }
    
    public void reset() {
        startTime = 0;
        stopTime = 0;
        lastMark = 0;
        
        markedTimes.clear();
    }

    public void mark(String comment) {
        markedTimes.add(new StopWatchEntry(System.currentTimeMillis(), System.currentTimeMillis() - lastMark, comment));

        lastMark = System.currentTimeMillis();
    }

    public List<StopWatchEntry> entries() {
        return Collections.unmodifiableList(markedTimes);
    }

    public void print() {
        Logger.getLogger(CodeStopWatch.class.getName()).log(Level.INFO, "Stopwatch for {0} started in {1}", new Object[]{reason, new Date(startTime)});
        Logger.getLogger(CodeStopWatch.class.getName()).log(Level.INFO, "-----------------------------------------");

        long lastLoggedTime = startTime;

        for (StopWatchEntry entry : markedTimes) {
            Logger.getLogger(CodeStopWatch.class.getName()).log(Level.INFO, "{0}\t{1}\t{2}s", new Object[]{entry.message, new Date(entry.timestamp), new BigDecimal((entry.timestamp - lastLoggedTime) / 1000.0).setScale(2, RoundingMode.CEILING).toString()});
            lastLoggedTime = entry.timestamp;
        }

        Logger.getLogger(CodeStopWatch.class.getName()).log(Level.INFO, "-----------------------------------------");

        Logger.getLogger(CodeStopWatch.class.getName()).log(Level.INFO, "Stopwatch for {0} stopped in {1} after {2}s", new Object[]{reason, new Date(stopTime), new BigDecimal((stopTime - startTime) / 1000.0).setScale(2, RoundingMode.CEILING).toString()});
    }

    public void destroy() {
        if (INSTANCE.get() != null) {
            INSTANCE.remove();
        }
    }

    public static CodeStopWatch instance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new CodeStopWatch());
        }

        return INSTANCE.get();
    }

    public class StopWatchEntry {

        final long timestamp;
        final long timeFromLastEntry;
        final String message;

        public StopWatchEntry(long timestamp, long timeFromLastEntry, String message) {
            this.timestamp = timestamp;
            this.timeFromLastEntry = timeFromLastEntry;
            this.message = message;
        }

    }

}
