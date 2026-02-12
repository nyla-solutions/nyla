package nyla.solutions.office.fop;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfFileDecoratorTest {

    private final String fopXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            
              <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin="2cm">
                  <fo:region-body/>
                </fo:simple-page-master>
              </fo:layout-master-set>
            
              <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">
                  <fo:block font-size="14pt">
                    Hello World
                  </fo:block>
                </fo:flow>
              </fo:page-sequence>
            
            </fo:root>
            """;

    private PdfFileDecorator subject;

    @Mock
    private Textable textable;
    private final String filePath = IO.tempDir()+"/fo.pdf";

    @BeforeEach
    void setUp() {
        subject =  new PdfFileDecorator(textable,filePath);
    }

    @Test
    void given_xml_when_get_then_product_pdf() {

        when(textable.getText()).thenReturn(fopXml);
        var file = subject.getFile();

        assertThat(file).isNotNull();
        assertThat(file).exists();
    }
}