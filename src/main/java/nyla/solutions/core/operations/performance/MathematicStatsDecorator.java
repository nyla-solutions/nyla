package nyla.solutions.core.operations.performance;

import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.util.stats.MathematicStats;

/**
 * @author Gregory Green
 */
public class MathematicStatsDecorator implements TextDecorator<MathematicStats>
{
    private final MathematicStats stats;

    public MathematicStatsDecorator(MathematicStats stats)
    {
        this.stats = stats;
    }

    /**
     * @return the text
     */
    @Override
    public String getText()
    {
        return new StringBuilder()

                .append("mean ms").append('\t').append(toMilliseconds(stats.mean()))
                .append('\n')
                .append("min ms").append('\t').append(toMilliseconds(stats.min()))
                .append('\n')
                .append("max ms").append('\t').append(toMilliseconds(stats.max()))
                .append('\n')
                .append("70th ms").append('\t').append(toMilliseconds(stats.percentile(70)))
                .append('\n')
                .append("90th ms").append('\t').append(toMilliseconds(stats.percentile(90)))
                .append('\n')
                .append("99.9th ms").append('\t').append(toMilliseconds(stats.percentile(99.9)))
                .append('\n')
                .append("99.999th ms").append('\t').append(toMilliseconds(stats.percentile(99.999)))
                .append('\n')
                .append("stddev ms").append('\t').append(toMilliseconds(stats.stdDev()))
                .toString();
    }

    protected double toMilliseconds(double nanoseconds)
    {
        return nanoseconds/1000000.00;
    }

    /**
     * @return the targeted object
     */
    @Override
    public MathematicStats getTarget()
    {
        return null;
    }
}
