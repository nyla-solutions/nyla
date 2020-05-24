package nyla.solutions.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

class PresenterTest
{

    @Test
    public void test_getTexts()
    {
        Presenter presenter = Presenter.getPresenter(this.getClass());
        String[] texts = presenter.getTexts("exampleTexts",",");
        assertNotNull(texts);
        assertTrue(texts != null  && texts.length > 0);

    }

}