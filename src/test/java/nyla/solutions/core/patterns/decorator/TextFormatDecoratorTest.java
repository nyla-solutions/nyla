package nyla.solutions.core.patterns.decorator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TextFormatDecoratorTest
{

    @Test
    void getText()
    {
        TextFormatDecorator subject = new TextFormatDecorator();
        assertNull(subject.getText());

    }

    @Test
    void getText_target()
    {
        TextFormatDecorator subject = new TextFormatDecorator();
        subject.setTarget(new StringText("hello"));
        assertEquals("hello",subject.getText());

    }

    @Test
    void getText_target_map()
    {
        TextFormatDecorator subject = new TextFormatDecorator();
        subject.setTarget(new StringText("hello ${name}"));
        Map<Object, Object> map = new HashMap<>();
        map.put("name","Joe");
        subject.setMap(map);
        assertEquals("hello Joe",subject.getText());
    }
}