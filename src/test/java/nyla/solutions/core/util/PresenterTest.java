package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getPresenter_withLocale() {
        assertNotNull(Presenter.getPresenter(Locale.US));
    }

    @Test
    void getPresenter_withLocaleNull_throwsException() {
        assertThrows(IllegalArgumentException.class,
                ()-> Presenter.getPresenter((Locale)null));
    }
}