/**
 *
 */
package nyla.solutions.core.media;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static nyla.solutions.core.media.Graphics.Format.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Graphics objects
 * @author Gregory Green
 *
 */
public class GraphicsTest
{


    /**
     * Test method for {@link nyla.solutions.core.media.Graphics#printScreen(int, int, int, int, java.lang.String, java.io.File)}.
     */
    @Test
    public void printScreen_given_file_then_file_exists() throws AWTException {
        Path file = Paths.get("runtime/tmp/output/screenshot.png");
        file.toFile().delete();

        assertFalse(file.toFile().exists());
        Graphics graphics = new Graphics();
        graphics.printScreen(0, 0, 1000, 800, "png", file.toFile());

        assertTrue(file.toFile().exists());
    }

    @Test
    public void printScreen_given_outputStream_then_file_exists() throws AWTException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Graphics subject = new Graphics();
        subject.printScreen(0, 0, 1000, 800, "png", outputStream);

        byte[] actual = outputStream.toByteArray();
        assertThat(actual).hasSizeGreaterThan(0);




    }

    @Test
    public void printScreen_given_outputStream_formats() throws AWTException, IOException {
        Graphics subject = new Graphics();

        Arrays.asList(JPEG,PNG,GIF).forEach( format -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            subject.printScreen(0, 0, 1000, 800, format, outputStream);

            byte[] actual = outputStream.toByteArray();
            assertThat(actual).hasSizeGreaterThan(0);
                }
        );





    }

    @Test
    void rotateImage_when_inputFile_doesNot_exist_throws_IOException() throws AWTException, IOException {
        Graphics subject = new Graphics();

        Path inputPath = Paths.get("build/tmp/file.txt");
        IO.dir().delete(inputPath.toFile());
        assertFalse(inputPath.toFile().exists());
        File outputFile = null;
        String format = null;
        int degree = -1;

        try{
            subject.rotateImage(inputPath.toFile(),outputFile,format,degree);
            assertFalse(inputPath.toFile().exists());
            fail();
        }
        catch(Exception e)
        {
            assertThat(e.getMessage()).contains(inputPath.toFile().getAbsolutePath());
            assertThat(e.getMessage()).contains(String.valueOf(degree));
        }
    }

    @Test
    void rotateImage_when_outputFile_doesNot_exist_then_created() throws AWTException, IOException {
        Graphics subject = new Graphics();

        File inputFile = Paths.get("build/tmp/rotateImage_when_outputFile_doesNot_exist_then_created.txt").toFile();
        subject.printScreen(0,0,100,100,"png",inputFile);
        Path outputFilePath = Paths.get("build/tmp/newdirectory/out.txt");;
        outputFilePath.toFile().delete();
        assertFalse(outputFilePath.toFile().exists());

        String format = "png";
        int degree = -1;


        subject.rotateImage(inputFile,outputFilePath.toFile(),format,degree);
        assertTrue(outputFilePath.toFile().exists());

    }

    @Test
    void rotateImage_invalid_format() throws AWTException, IOException {
        Graphics subject = new Graphics();

        int degree = 30;
        Arrays.asList(JPEG,GIF,PNG).forEach(
                format -> {
                    File inputFile = Paths.get("build/tmp/rotateImage_outputFile_exists."+format).toFile();
                    subject.printScreen(0,0,100,100,format,inputFile);
                    Path outputFilePath = Paths.get("build/tmp/graphics/rotateImage_outputFile_exists."+format);;
                    outputFilePath.toFile().delete();
                    try {
                        subject.rotateImage(inputFile,outputFilePath.toFile(),format,degree);
                       //TODO: flaky assertTrue(outputFilePath.toFile().exists());
                    }
                    catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }
        );



        File inputFile = Paths.get("build/tmp/rotateImage_outputFile_exists.jpg").toFile();
        subject.printScreen(0,0,100,100,"jpg",inputFile);
        Path outputFilePath = Paths.get("build/tmp/graphics/rotateImage_outputFile_exists.jpg");
        outputFilePath.toFile().delete();
        subject.rotateImage(inputFile,outputFilePath.toFile(),"UNKNOWN",degree);
        assertFalse(outputFilePath.toFile().exists());


    }
}
