# CodeStopWatch
A Java library that helps developers benchmark their code

# Usage
Using CodeStopWatch is so simple, first of all you add it to your POM file:

```xml
<dependency>
    <groupId>ps.purelogic</groupId>
    <artifactId>CodeStopWatch</artifactId>
    <version>1</version>
</dependency>
```

Then you can use CodeStopWatch in one of the following use cases.

## Testing Code Speed as a Whole
You can test any code speed with stopwatch by using **benchmark** method as follows:

```java
long totalTimeInMillis = CodeStopWatch.instance().benchmark(() -> {
            //Your testing code goes here
        }, "Your Purpose of testing");
```

Then after you're done you can call:

```java
CodeStopWatch.instance().print();
```

## Testing Code Speed with Marks
You can mark parts of code as you go to benchmark which part is slow or suffering from poor performance:

```java
CodeStopWatch.instance().start("We want to test some code");
//Code block 1
CodeStopWatch.instance().mark("Finished executing block 1");
//Code block 2
CodeStopWatch.instance().mark("Finished executing block 2");
//Code block 3
CodeStopWatch.instance().mark("Finished executing block 3");
//Code block 4
CodeStopWatch.instance().mark("Finished executing block 4");

long totalRunTimeInMillis = CodeStopWatch.instance().stop();

CodeStopWatch.instance().print();
```

Print output in console or in log file will look like this:
```
INFO: Stopwatch for We want to test some code started in 10/4/22 7:38 PM
INFO: -----------------------------------------
INFO: Finished executing block 1	10/4/22 7:38 PM	1.01s
INFO: Finished executing block 2	10/4/22 7:38 PM	1.20s
INFO: Finished executing block 3	10/4/22 7:38 PM	0.81s
INFO: Finished executing block 4	10/4/22 7:42 PM	2.00s
INFO: -----------------------------------------
INFO: Stopwatch for We want to test some code stopped in 10/4/22 7:38 PM after 5.01s
```

#Other Utility Methods
| Method  | Descro[topm |
| ------------- | ------------- |
| reset  | Resets stopwatch parameters  |
| destroy  | Destroys current instance  |
