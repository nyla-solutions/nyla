package nyla.solutions.core.io.csv;

import nyla.solutions.core.io.IO;
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
        return new TreeSet(new CsvOrderByColComparator(orderByFieldCol));
    }

    public CsvSelectBuilder groupBy(int csvColHeader) {
        groupByFieldCol = csvColHeader;

        if(groupByMap == null)
            groupByMap = new HashMap<>();

        orderByList.forEach(list -> {
            var groupByValue = Organizer.getByIndex(list,groupByFieldCol);
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

    public Collection<Collection<List<String>>> build() {

        if(this.groupByMap == null)
        {
            if(orderByList == null || orderByList.isEmpty())
            {
                return (Collection)csvReader.getData();

            }


            return singletonList(orderByList);
        }

        return (Collection)this.groupByMap.values();
    }

    public List<String> buildCsvText() {

        var aggregatedOutput = build();
        ArrayList<String> returnList  = new ArrayList<>();
        for (Collection<List<String>> set: aggregatedOutput) {
            var csvOutput = new StringBuilder();

            set.forEach( line -> {
                csvOutput.append(CsvWriter.toCSV(line.toArray())).append(newline());
            });

            returnList.add(csvOutput.toString());
        }

        return returnList;
    }
}
