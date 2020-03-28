/**
 *
 */
package nyla.solutions.core.media;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author Gregory Green
 *
 */
public class GraphicsTest
{

    /**
     * Test method for {@link nyla.solutions.core.media.Graphics#printScreen(int, int, int, int, java.lang.String, java.io.File)}.
     */
    @Test
    public void testPrintScreenIntIntIntIntStringFile()
    {
        Graphics.printScreen(0, 0, 1000, 800, "png", new File("runtime/tmp/output/screenshot.png"));
    }

}
