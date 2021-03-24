package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.creational.generator.FirstNameCreator;
import nyla.solutions.core.patterns.creational.generator.LastNameCreator;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MappedTextFormatDecoratorTest
{
    MappedTextFormatDecorator subject;
    Map<String, Textable> map = new HashMap<>();
    String fn = new FirstNameCreator().create();
    String ln = new LastNameCreator().create();

    @BeforeEach
    public void setUp()
    {
        subject = new MappedTextFormatDecorator();

        map.put("fn", new StringText(fn));
        map.put("ln", new StringText(ln));
    }
    @Test
    public void test_format_with_no_template()
    {
        assertThrows(RequiredException.class, () -> subject.getText());
    }

    @Test
    public void test_map_template_url()
    {
        subject.setMap(map);
        String url = Text.build("file://",
                Paths.get(".").toFile().getAbsolutePath()
                ,"/src/test/resources/templates/decorators/MappedTextFormatDecoratorTest.txt");
        subject.setTemplateUrl(url);

        String text = subject.getText();
        assertThat(text).contains(fn);
        assertThat(text).contains(ln);
    }


    @Test
    public void test_map_template()
    {
        subject.setMap(map);
        String url = Text.build("file://",
                Paths.get(".").toFile().getAbsolutePath()
                ,"/src/test/resources/templates/decorators/MappedTextFormatDecoratorTest.txt");
        subject.setTemplate("I love ${fn} ${ln}");

        String text = subject.getText();
        assertThat(text).contains(fn);
        assertThat(text).contains(ln);
    }

}