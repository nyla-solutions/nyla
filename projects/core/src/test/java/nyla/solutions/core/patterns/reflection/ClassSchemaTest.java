package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClassSchemaTest
{
    private ClassSchema subject;

    enum ExampleEnum{
        HELLO
    }

    @Mock
    private ClassSchemaVisitor visitor;


    @BeforeEach
    public void setUp()
    {
        subject = new ClassSchema(UserProfile.class);
    }
    @Test
    void getClassType()
    {
        subject = new ClassSchema(UserProfile.class);
        assertEquals(ClassType.generic, subject.getClassType());
    }

    @Test
    void getClassTypeEnum()
    {

        subject = new ClassSchema(ExampleEnum.class);
        assertEquals(ClassType.ENUM, subject.getClassType());
    }
    @Test
    void getClassTypePrimitives()
    {
        subject = new ClassSchema(int.class);
        assertEquals(ClassType.primitive,subject.getClassType());

    }
    @Test
    void getClassTypeArrays()
    {

        subject = new ClassSchema(String[].class);
        assertEquals(ClassType.array,subject.getClassType());

    }
    @Test
    void getClassTypeDatesCalendar()
    {
        subject = new ClassSchema(Calendar.class);
        assertEquals(ClassType.calendar,subject.getClassType());
    }
    @Test
    void getClassTypeDatesDate()
    {

        subject = new ClassSchema(Date.class);
        assertEquals(ClassType.date,subject.getClassType());
    }
    @Test
    void getClassTypeDatesTime()
    {

        subject = new ClassSchema(Time.class);
        assertEquals(ClassType.time,subject.getClassType());
    }
    @Test
    void getClassTypeDatesTimestamp()
    {

        subject = new ClassSchema(Timestamp.class);
        assertEquals(ClassType.timestamp,subject.getClassType());
    }
    @Test
    public void test_arrayList()
    {
        ClassSchema subject = new ClassSchema(ArrayList.class);
        assertEquals(ClassType.NonPrimitiveJava,subject.getClassType());

    }
    @Test
    void accept()
    {
        subject.accept(visitor);
        verify(visitor).visitClass(any());
    }

    @Test
    void getObjectClassName()
    {
        assertThat(subject.getObjectClassName())
                .isEqualTo(UserProfile.class.getName());
    }

    @Test
    void getFieldSchemas()
    {
        var actual = subject.getFieldSchemas();
        assertThat(actual).isNotEmpty();
    }
}