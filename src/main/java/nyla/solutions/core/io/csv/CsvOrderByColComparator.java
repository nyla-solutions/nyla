package nyla.solutions.core.io.csv;

import nyla.solutions.core.util.Organizer;

import java.util.List;

import static nyla.solutions.core.util.Organizer.getByIndex;

/**
 * @author gregory green
 */
public class CsvOrderByColComparator implements java.util.Comparator<List<String>> {
    private final int orderByFieldCol;

    public CsvOrderByColComparator(int orderByFieldCol) {
        this.orderByFieldCol = orderByFieldCol;
    }

    @Override
    public int compare(List<String> o1, List<String> o2) {

        var value1 = getByIndex(o1,orderByFieldCol);
        var value2 = getByIndex(o2,orderByFieldCol);

        if(value1 == value2)
            return -1;

        if(value1 == null)
            return -1;

        if(value2 == null)
            return 1;

        var compared = value1.compareTo(value2);
        if(compared > 0)
            return 1;
        else if(compared < 0)
            return -1;
        return -1;
    }


}
