package nyla.solutions.core.patterns.expression.bre;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.expression.ComparableExpression;
import nyla.solutions.core.patterns.expression.OrExpression;
import nyla.solutions.core.patterns.expression.TrueFalseBooleanExpression;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleEngineTest
{
    private final Integer value = 10;
    private final String ruleName = "MoreThan10Always";
    private Map<String, Function<Boolean, Comparable<?  extends Number>>> rules =
            Organizer.toMap(ruleName, new TrueFalseBooleanExpression(true));

    @Test
    void apply()
    {
         Map<String, Function<Boolean, Comparable<?  extends Number>>> rules =
                Organizer.toMap(ruleName, new TrueFalseBooleanExpression(true));

        BusinessRuleEngine<Comparable<?  extends Number>,Boolean> bre = new BusinessRuleEngine(rules);
        assertEquals(true,bre.applyForRule(ruleName,value));
    }


    /**
     *
     * The rules for Abnormal Vital Statistics: where "+
     *                     " (statName = 'heartRate' and (value < 55 or value >  105))       "+
     *                     " or                                                                "+
     *                     " (statName = 'bodyTemperature' and (value < 95 or value >  103)) "+
     *                     " or                                                                "+
     *                     " (statName = 'respirationRate' and (value < 12 or value >  16)) "+
     *                     " or                                                                "+
     *                     " (statName = 'bloodPressureDiastolic' and value < 80 )"+
     *                     " or                                                                "+
     *                     " (statName = 'bloodPressureSystolic' and value > 130)
     */
    @Test
    void vitalSignsRules()
    {
        Map<String, Function<Boolean, Comparable<?  extends Number>>> rules =
                Organizer.toMap("heartRate", new OrExpression<Integer>(
                        ComparableExpression.lessThan(55),
                        ComparableExpression.greaterThan(105)
                        ),
                        "bodyTemperature",new OrExpression<Integer>(
                                ComparableExpression.lessThan(95),
                                ComparableExpression.greaterThan(103)
                        )
                        , "respirationRate",new OrExpression<Integer>(
                                ComparableExpression.lessThan(12),
                                ComparableExpression.greaterThan(16)
                        )
                        ,
                        "bloodPressureDiastolic", ComparableExpression.lessThan(80)
                        ,
                        "bloodPressureSystolic", ComparableExpression.greaterThan(130)
                        );

        BusinessRuleEngine<Comparable<?  extends Number>,Boolean> abnormalVitalBre = new BusinessRuleEngine(rules);
        assertEquals(true,abnormalVitalBre.applyForRule("bodyTemperature",200));

        assertEquals(false,abnormalVitalBre.applyForRule("bodyTemperature",98));

    }

    @Test
    void builder()
    {
        /**
         *
         * The rules for Abnormal Vital Statistics: where "+
         *                     " (statName = 'heartRate' and (value < 55 or value >  105))       "+
         *                     " or                                                                "+
         *                     " (statName = 'bodyTemperature' and (value < 95 or value >  103)) "+
         *                     " or                                                                "+
         *                     " (statName = 'respirationRate' and (value < 12 or value >  16)) "+
         *                     " or                                                                "+
         *                     " (statName = 'bloodPressureDiastolic' and value < 80 )"+
         *                     " or                                                                "+
         *                     " (statName = 'bloodPressureSystolic' and value > 130)
         */

        BusinessRuleEngine<Comparable<?  extends Number>,Boolean> abnormalVitalBre = BusinessRuleEngine
                .builder()
                .rule("heartRate",
                        new OrExpression<Integer>(ComparableExpression.lessThan(55),
                                ComparableExpression.greaterThan(105)))
                .rule("bodyTemperature",new OrExpression<Integer>(
                    ComparableExpression.lessThan(95),
                    ComparableExpression.greaterThan(103)))
                .rule( "respirationRate",new OrExpression<Integer>(
                    ComparableExpression.lessThan(12),
                    ComparableExpression.greaterThan(16)))
                .rule("bloodPressureDiastolic", ComparableExpression.lessThan(80))
                .rule("bloodPressureSystolic", ComparableExpression.greaterThan(130))
                .build();

        assertEquals(true,abnormalVitalBre.applyForRule("bodyTemperature",200));

        assertEquals(false,abnormalVitalBre.applyForRule("bodyTemperature",98));
    }

    @Test
    void givenNullMap_WhenApply_Then_ReturnException()
    {
        assertThrows(RequiredException.class, () ->  new BusinessRuleEngine(null));
    }

    @Test
    void givenEmptyMap_WhenApply_Then_ReturnException()
    {
        assertThrows(RequiredException.class, () ->  new BusinessRuleEngine(new HashMap<>()));
    }

    @Test
    void givenNotMatchingRule_WhenApply_ThenNull()
    {
        BusinessRuleEngine<Comparable<?  extends Number>,Boolean> bre = new BusinessRuleEngine(rules);
        assertNull(bre.applyForRule("doesNotExists",value));
    }

    @Test
    void givenNullRule_WhenApply_ThenNull()
    {
        BusinessRuleEngine<Comparable<?  extends Number>,Boolean> bre = new BusinessRuleEngine(rules);
        assertNull(bre.applyForRule(null,value));
    }
}