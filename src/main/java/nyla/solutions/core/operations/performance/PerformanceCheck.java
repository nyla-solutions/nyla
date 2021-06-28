package nyla.solutions.core.operations.performance;

import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.util.stats.MathematicStats;
import nyla.solutions.core.util.stats.Mathematics;

/**
 * @author Gregory Green
 */
public class PerformanceCheck
{
    private final BenchMarker marker;
    private final MathematicStats stats;
    private final TextDecorator<MathematicStats> decorator;


    public PerformanceCheck(BenchMarker marker, MathematicStats stats){
        this(marker,stats,new MathematicStatsDecorator(stats));
    }
    public PerformanceCheck(BenchMarker marker, MathematicStats stats, TextDecorator<MathematicStats> decorator)
    {
        this.marker = marker;
        this.stats = stats;
        decorator.setTarget(stats);
        this.decorator = decorator;
    }

    public PerformanceCheck(BenchMarker marker, int capacity)
    {
        this(marker, new MathematicStats(capacity, new Mathematics()));
    }

    public void perfCheck(Runnable r)
    {
        try {
            marker.executeBenchMark(r,stats);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getReport()
    {
        return decorator.getText();
    }
}
