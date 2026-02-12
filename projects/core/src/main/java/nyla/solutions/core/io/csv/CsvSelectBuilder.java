package nyla.solutions.core.io.csv;

import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.util.Organizer;

import java.util.*;

import static java.util.Collections.singletonList;
import static nyla.solutions.core.io.IO.newline;

/**
 * @author gregory green
 */
public class CsvSelectBuilder {

    private final CsvReader csvReader;
    private int orderByFieldCol;
    private int groupByFieldCol;

    private TreeSet<List<String>> orderByList;
    private Map<String,TreeSet<List<String>>> groupByMap;
    private BooleanExpression<List<String>> predicate;


    public CsvSelectBuilder(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public CsvSelectBuilder orderBy(int csvColHeader) {
        orderByFieldCol = csvColHeader;

        orderByList = constructOrderByList();

        orderByList.addAll(csvReader.getData());
        return this;
    }

    private TreeSet<List<String>> constructOrderByList() {
        return new TreeSet<List<String>>(new CsvOrderByColComparator(orderByFieldCol));
    }

    public CsvSelectBuilder groupBy(int csvColHeader) {
        groupByFieldCol = csvColHeader;

        if(groupByMap == null)
            groupByMap = new HashMap<>();

        orderByList.forEach(list -> {
            String groupByValue = Organizer.arrange(list).getByIndex(groupByFieldCol);
            var groupList = this.groupByMap.get(groupByValue);

            if(groupList == null)
            {
                groupList = constructOrderByList();
            }
            groupList.add(list);
            this.groupByMap.put(groupByValue,groupList);

        });

        return this;
    }

    public CsvSelectBuilder where(BooleanExpression<List<String>> predicate) {
        this.predicate = predicate;
        return this;
    }

    public Collection<List<String>> build() {

        if(this.groupByMap == null)
        {
            if(orderByList == null || orderByList.isEmpty())
            {
                return from((Collection)csvReader.getData());

            }

            return from(orderByList);
        }

        return fromGroup(this.groupByMap.values());
    }

    private Collection<List<String>> fromGroup(Collection<TreeSet<List<String>>> values) {
        var returnList = new ArrayList<List<String>>();
        for (TreeSet<List<String>> list : values) {
            returnList.addAll(from(list));
        }
        return returnList;
    }

    private Collection<List<String>> from(Collection<List<String>> fromList) {
        if(predicate != null)
        {
            var filteredList = new ArrayList<List<String>>();
            for (List<String> list : fromList) {
                if(predicate.apply(list))
                {
                    filteredList.add(list);
                }
            }
            return filteredList;
        }
        return fromList;
    }

    public List<String> buildCsvText() {

        Collection<List<String>> aggregatedOutput = build();
        ArrayList<String> returnList  = new ArrayList<>();
        for (List<String> lines: aggregatedOutput) {
            var csvOutput = new StringBuilder();
            csvOutput.append(CsvWriter.toCSV(lines.toArray())).append(newline());
            returnList.add(csvOutput.toString());
        }

        return returnList;
    }


}
