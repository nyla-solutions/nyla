package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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