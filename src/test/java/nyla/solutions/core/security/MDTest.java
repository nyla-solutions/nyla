package nyla.solutions.core.security;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MDTest
{

    @Test
    void checksum()
    {
        UserProfile user = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        BigInteger actual = MD.checksum(user);

        System.out.println(actual);
        assertNotNull(actual);

    }

    @Test
    void checksum_None_serializable()
    {
        String id1 = "id1";
        MdQaDomain domain = new MdQaDomain(id1);

        BigInteger actual = MD.checksum(domain);
        System.out.println(actual);
        assertNotNull(actual);
    }
    @Test
    void checksum_Two_Non_serialized_Not_Equals()
    {
        String id1 = "id1";
        String id2 = "id2";

        assertNotEquals(MD.checksum(new MdQaDomain(id1)),MD.checksum(new MdQaDomain(id2)));
    }
    @Test
    void checksum_Two_Non_serialized_Are_Equals()
    {
        String id1 = "id1";
        String id2 = "id1";

        assertEquals(MD.checksum(new MdQaDomain(id1)),MD.checksum(new MdQaDomain(id2)));
    }
    public class MdQaDomain{
        private final String id;

        public MdQaDomain(String id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder("MdQaDomain{");
            sb.append("id='").append(id).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}