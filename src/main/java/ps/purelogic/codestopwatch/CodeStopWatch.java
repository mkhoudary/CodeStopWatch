/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ps.purelogic.codestopwatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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

    public long benchmark(String reason, Runnable runnable) {
        start(reason);

        mark("Started execution");

        runnable.run();

        mark("Finished execution");

        return stop();
    }

    public long[] compare(String reason, Runnable... runnables) {
        long[] executions = new long[runnables.length];

        start(String.format(reason));

        for (int i = 0; i < executions.length; i++) {
            mark(String.format("Executing scenario %d", i + 1));

            runnables[i].run();

            executions[i] = mark(String.format("Finished executing scenario %d", i + 1));
        }

        stop();

        return executions;
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

    public long mark(String comment) {
        long timeTaken = System.currentTimeMillis() - lastMark;

        markedTimes.add(new StopWatchEntry(System.currentTimeMillis(), timeTaken, comment));

        lastMark = System.currentTimeMillis();

        return timeTaken;
    }

    public List<StopWatchEntry> entries() {
        return Collections.unmodifiableList(markedTimes);
    }

    public void print() {
        print(false);
    }

    public void print(boolean warning) {
        Level loggingLevel = Level.INFO;

        if (warning) {
            loggingLevel = Level.WARNING;
        }

        String startLabel = String.format("Stopwatch for %s started in %s", reason, new Date(startTime));

        Logger.getLogger(CodeStopWatch.class.getName()).log(loggingLevel, startLabel);
        Logger.getLogger(CodeStopWatch.class.getName()).log(loggingLevel, "-----------------------------------------");

        long lastLoggedTime = startTime;

        for (StopWatchEntry entry : markedTimes) {
            String durationLabel = String.format("%s\t%s\t%s s",entry.message, new Date(entry.timestamp), new BigDecimal((entry.timestamp - lastLoggedTime) / 1000.0).setScale(2, RoundingMode.CEILING).toString());
            Logger.getLogger(CodeStopWatch.class.getName()).log(loggingLevel, durationLabel);
            lastLoggedTime = entry.timestamp;
        }

        Logger.getLogger(CodeStopWatch.class.getName()).log(loggingLevel, "-----------------------------------------");

        String finishTitle = String.format("Stopwatch for %s stopped in %s after %s s", reason, new Date(stopTime), new BigDecimal((stopTime - startTime) / 1000.0).setScale(2, RoundingMode.CEILING).toString());
        Logger.getLogger(CodeStopWatch.class.getName()).log(loggingLevel, finishTitle);

    }

    public void printIfExceeds(long timeInMillis) {
        if (stopTime - startTime > timeInMillis) {
            print(true);
        }
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
