package nyla.solutions.core.io.csv;

import nyla.solutions.core.util.Organizer;

import java.util.List;


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

        String value1 = Organizer.arrange(o1).getByIndex(orderByFieldCol);
        String value2 = Organizer.arrange(o2).getByIndex(orderByFieldCol);

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
