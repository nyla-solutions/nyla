package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Text;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Randomly generate Map key and value to text based on given settings.
 *
 * @author Gregory Green
 */
public class MapTextCreator implements Creator<Map<String,String>>
{
    private final int size;
    private final int keyPadLength;
    private final int valueLength;
    private final String seedText;

    public MapTextCreator(int size, int keyPadLength, int valueLength, String seedText)
    {
        this.size = size;
        this.keyPadLength = keyPadLength;
        this.valueLength = valueLength;
        this.seedText = seedText;
    }

    public static MapTextCreatorBuilder builder()
    {
        return new MapTextCreatorBuilder();
    }

    /**
     * Internal builder
     */
    public static class MapTextCreatorBuilder{
        private int size;
        private int keyPadLength;
        private int valueLength;
        private String seedText;
        public MapTextCreatorBuilder size(int size)
        {
            this.size = size;
            return this;
        }

        public MapTextCreatorBuilder keyPadLength(int keyPadLength)
        {
            this.keyPadLength = keyPadLength;
            return this;
        }
        public MapTextCreatorBuilder valueLength(int valueLength)
        {
            this.valueLength = valueLength;
            return this;
        }
        public MapTextCreatorBuilder seedText(String seedText)
        {
            this.seedText = seedText;
            return this;
        }
        public MapTextCreator build()
        {
            return new MapTextCreator(size, keyPadLength,valueLength, seedText);
        }


    }

    /**
     * @return the create object
     */
    @Override
    public Map<String, String> create()
    {
        Map<String,String> map = new HashMap<String,String>(this.size);

        for (int i = 0; i < size; i++) {
            map.put(new StringBuilder().append(i)
                            .append(Text.fixedLength(seedText, keyPadLength))
                                       .toString(),
                    Text.fixedLength(seedText,valueLength));
        }

        return map;
    }
}
