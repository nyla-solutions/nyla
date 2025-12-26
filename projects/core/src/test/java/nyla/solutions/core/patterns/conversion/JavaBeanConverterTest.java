package nyla.solutions.core.patterns.conversion;

import net.bytebuddy.dynamic.TargetType;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.util.JavaBean;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JavaBeanConverterTest {

    private final int expectedId = 99;
    private final String expectedValue = "99";
    private final SourceClass source = JavaBeanGeneratorCreator.of(SourceClass.class).create();
    private TargetRecord target = JavaBeanGeneratorCreator.of(TargetRecord.class).create();

    @Test
    void convertRecords() {
        SourceRecord source = new SourceRecord(expectedId,expectedValue);
        var subject = new JavaBeanConverter<SourceRecord,TargetRecord>(TargetRecord.class);

        var actual = subject.convert(source);
        assertThat(actual).isNotNull();
        assertThat(actual.id).isEqualTo(expectedId);
        assertThat(actual.value).isEqualTo(expectedValue);
    }

    @Test
    void convertJavaBeans() {

        var subject = new JavaBeanConverter<SourceClass, TargetClass>(TargetClass.class);
        var actual = subject.convert(source);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(source.getId());
        assertThat(actual.getValue()).isEqualTo(source.getValue());

    }

    @Test
    void convertJavaBeanToRecord() {
        JavaBeanConverter<SourceClass,TargetRecord> subject =
                JavaBean.converter(TargetRecord.class);

        var actual = subject.convert(source);

        System.out.println(actual);
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(source.getId());
        assertThat(actual.value()).isEqualTo(source.getValue());
    }

    @Test
    public void convertRecordToJavaBean(){

        JavaBeanConverter<TargetRecord,SourceClass> subject =
                JavaBean.converter(SourceClass.class);

        var actual = subject.convert(target);

        System.out.println(actual);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(target.id());
        assertThat(actual.getValue()).isEqualTo(target.value());
    }


    public static record SourceRecord(int id, String value){}
    public static record TargetRecord(int id,String value){}


    public static class SourceClass
    {
        private int id;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TargetClass
    {
        private int id;
        private String value;
        private String extra;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}