package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexTypeSchemaTest {

    private String fieldName = "user";

    @Test
    void setFieldName() {
        var subject = new ComplexTypeSchema(UserProfile.class);
        subject.setFieldName(fieldName);

        assertEquals(fieldName, subject.getFieldName());

    }

    @Test
    void setClassName() {
        var subject = new ComplexTypeSchema(UserProfile.class);
        subject.setClassName(UserProfile.class.getName());

        assertEquals(UserProfile.class.getName(), subject.getClassName());
    }

    @Test
    void getClassType() {
        var subject = new ComplexTypeSchema(fieldName, UserProfile.class);
        subject.setClassName(UserProfile.class.getName());

        assertEquals(ClassType.generic, subject.getClassType());
    }

    @Test
    void getFieldClass() {
        var subject = new ComplexTypeSchema(fieldName, String.class);

        assertEquals(String.class, subject.getFieldClass());
    }

    @Test
    void getFieldClass_array() {
        String [] field = {"hello"};
        var subject = new ComplexTypeSchema(fieldName, field.getClass());

        assertEquals(ClassType.array, subject.getClassType());
    }

    @Test
    void getFieldClass_date() {
        Date field = new Date();
        var subject = new ComplexTypeSchema(fieldName, field.getClass());

        assertEquals(ClassType.date, subject.getClassType());
    }

    @Test
    void getFieldClass_calendar() {
        Calendar field = Calendar.getInstance();
        var subject = new ComplexTypeSchema(fieldName, field.getClass());

        assertEquals(ClassType.calendar, subject.getClassType());
    }

    @Test
    void getFieldClass_timestamp() {
        Timestamp field = new Timestamp(System.currentTimeMillis());
        var subject = new ComplexTypeSchema(fieldName, field.getClass());

        assertEquals(ClassType.timestamp, subject.getClassType());
    }

    @Test
    void getFieldClass_time() {
        Time field = new Time(System.currentTimeMillis());
        var subject = new ComplexTypeSchema(fieldName, field.getClass());

        assertEquals(ClassType.time, subject.getClassType());
    }

    @Test
    void setFieldClass() {
        var subject = new ComplexTypeSchema(fieldName, String.class);
        subject.setFieldClass(Integer.class);

        assertEquals(Integer.class, subject.getFieldClass());
    }

    @Test
    void getFieldsTypes() {
    }
}