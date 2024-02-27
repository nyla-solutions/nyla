package nyla.solutions.core.data;

import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.JavaBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataRowJavaBeanCreateVisitorTest
{
    @Test
    public void testDataRowJavaBeanCreateVisitor()
    {
        DataRowCreator v = new DataRowCreator();

        UserProfile greg = new UserProfile();
        greg.setEmail("gregory.green@nyla-solutions.com");
        greg.setFirstName("Gregory");
        greg.setLastName("Green");
        greg.setLogin("greeng3");
        greg.setTitle("Senior Consultant");

        JavaBean.acceptVisitor(greg, v);

        DataRow dataRow = v.getDataRow();

        assertEquals(dataRow.retrieveString("email"), "gregory.green@nyla-solutions.com");
        assertEquals(dataRow.retrieveString("firstName"), "Gregory");
        assertEquals(dataRow.retrieveString("lastName"), "Green");
        assertEquals(dataRow.retrieveString("title"), "Senior Consultant");

    }

}
