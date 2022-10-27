package nyla.solutions.core.ds;

import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.Enumeration;
import java.util.Hashtable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LDAPTest
{

    private LDAP subject;
    private DirContext ctx;
    String url = "ldap://localhost";

    @BeforeEach
    void setUp()
    {
        ctx = mock(DirContext.class);

        this.subject = new LDAP(ctx,url);
    }

    @Test
    public void testSetupSecurityProperties()
    {
        char[] pwd = null;
        Hashtable<String, Object> properties = new Hashtable<>();

        String userDN = null;
        LDAP.setupSecurityProperties(properties, userDN, pwd);

        assertTrue(properties.isEmpty());

        userDN = "imani";
        pwd = "test".toCharArray();

        LDAP.setupSecurityProperties(properties, userDN, pwd);
        assertFalse(properties.isEmpty());
		
		/*
		 * java.naming.ldap.factory.socket = SSLConfigSocketFactory
contextFactory.environment[java.naming.security.protocol] = ssl
		 */

        properties.put(LDAP.JAVA_NAMING_PROVIDER_URL, "ldaps://helloworld:389");
        LDAP.setupSecurityProperties(properties, userDN, pwd);

        assertNull(properties.get("java.naming.ldap.factory.socket"));
        assertNull(properties.get("java.naming.security.protocol"));

        System.setProperty("LDAP_USE_SSL_CONFIG_FACTORY", "true");
        Config.reLoad();

        LDAP.setupSecurityProperties(properties, userDN, pwd);

        assertEquals(properties.get("java.naming.ldap.factory.socket"), SSLConfigSocketFactory.class.getName());
        assertEquals(properties.get("java.naming.security.protocol"), "ssl");
    }

    @Test
    void addAttribute() throws NamingException
    {
        Name name = new LdapName("ou=System");
        String attributeName = "name=hello";
        Attribute attribute = new BasicAttribute(attributeName);
        subject.addAttribute(name,attribute);
        verify(ctx).modifyAttributes(any(Name.class),anyInt(),any(Attributes.class));
    }

    @Test
    void setupBasicProperties() throws NamingException
    {
        Hashtable<String, Object> env = new Hashtable<>();
        LDAP.setupBasicProperties(env,url);

        assertThat(env.get("java.naming.ldap.deleteRDN")).isEqualTo("false");
        assertThat(env.get("java.naming.referral")).isEqualTo("follow");
        assertThat(env.get("java.naming.ldap.attributes.binary")).isEqualTo("photo jpegphoto jpegPhoto");
        assertThat(env.get("java.naming.ldap.derefAliases")).isEqualTo("finding");
        assertThat(env.get("java.naming.security.authentication")).isEqualTo("none");
        assertThat(env.get(LDAP.JAVA_NAMING_PROVIDER_URL)).isEqualTo(url);

        String expectedFactory ="expected";
        env.put("java.naming.factory.initial",expectedFactory);
        LDAP.setupBasicProperties(env,url);
        assertThat(env.get("java.naming.factory.initial")).isEqualTo(expectedFactory);
    }


    @Nested
    class SetupSecurityProperties
    {
        private Hashtable<String, Object> env;
        private String user = "user";
        private  char[] pwd = "pwd".toCharArray();

        @BeforeEach
        void setUp()
        {
            env = new Hashtable<>();
        }

        @Test
        void setupSecurityProperties()
        {

            LDAP.setupSecurityProperties(env,user,pwd);

            assertThat(env.get("java.naming.security.authentication")).isEqualTo("simple");
            assertThat(env.get("java.naming.security.principal")).isEqualTo(user);
            assertThat(env.get("java.naming.security.credentials")).isEqualTo(String.valueOf(pwd));
        }

        @Test
        void setupSecurityProperties_nosecurity()
        {
            LDAP.setupSecurityProperties(env,user,pwd);

            assertThat(env.get("java.naming.security.protocol")).isNotEqualTo("ssl");
            assertThat(env.get("java.naming.ldap.factory.socket")).isNotEqualTo(SSLConfigSocketFactory.class.getName());

        }
        @Test
        void setupSecurityProperties_LDAPS_noSSL_factory()
        {

            System.setProperty(LDAP.LDAP_USE_SSL_CONFIG_FACTORY_PROP,"false");
            env.put(LDAP.JAVA_NAMING_PROVIDER_URL,"ldaps://localhost");
            Config.reLoad();

            LDAP.setupSecurityProperties(env,user,pwd);
            assertThat(env.get("java.naming.security.protocol")).isNotEqualTo("ssl");
            assertThat(env.get("java.naming.ldap.factory.socket")).isNotEqualTo(SSLConfigSocketFactory.class.getName());


        }
        void setupSecurityProperties_LDAPS_SSL_factory()
        {

            System.setProperty(LDAP.LDAP_USE_SSL_CONFIG_FACTORY_PROP,"true");
            env.put(LDAP.JAVA_NAMING_PROVIDER_URL,"ldaps://localhost");
            Config.reLoad();

            LDAP.setupSecurityProperties(env,user,pwd);

            assertThat(env.get("java.naming.security.protocol")).isEqualTo("ssl");
            assertThat(env.get("java.naming.ldap.factory.socket")).isEqualTo(SSLConfigSocketFactory.class.getName());

        }
    }


    @Nested
    class ToSearchResults
    {
        @Test
        void toSearchResult_WhenNull_ThenThrowsNoDataFoundException()
        {
            NamingEnumeration<?> aNamingEnumeration = null;
            assertThrows(NoDataFoundException.class,
                    () -> LDAP.toSearchResult(null));
        }

        @Test
        void toSearchResult_WhenEmpty_ThenThrowsNoDataFoundException()
        {
            NamingEnumeration<?> namingEnumeration = mock(NamingEnumeration.class);
            when(namingEnumeration.hasMoreElements()).thenReturn(false);

            assertThrows(NoDataFoundException.class,
                    () -> LDAP.toSearchResult(namingEnumeration));
        }

        @Test
        void toSearchResult_WhenHasResults_ThenReturn()
        {
            NamingEnumeration namingEnumeration = mock(NamingEnumeration.class);
            when(namingEnumeration.hasMoreElements()).thenReturn(true);
            SearchResult expected = mock(SearchResult.class);
            when(namingEnumeration.nextElement()).thenReturn((Object)expected);
            SearchResult actual = LDAP.toSearchResult(namingEnumeration);
            assertEquals(expected,actual);
        }
    }
    @Test
    void toStringNamingEnumeration()
    {
        NamingEnumeration ne = mock(NamingEnumeration.class);

        assertEquals("",LDAP.toString((NamingEnumeration)null));
        String actual = LDAP.toString(ne);

        String expected = "";
        assertThat(actual).isEqualTo(expected);

        when(ne.hasMoreElements()).thenReturn(true).thenReturn(false);
        SearchResult searchResult = mock(SearchResult.class);
        String name = "attributeName";
        when(searchResult.getName()).thenReturn(name);
        when(ne.nextElement()).thenReturn(searchResult);
        actual = LDAP.toString(ne);
        assertThat(actual).contains(" name="+searchResult.getName());
    }

    @Test
    void toStringAttributes()
    {
        assertEquals("",LDAP.toString((Attributes)null));

        Attributes expectedAttributes = mock(Attributes.class);
        assertEquals("",LDAP.toString(expectedAttributes));

        NamingEnumeration namingEnumeration = mock(NamingEnumeration.class);
        when(expectedAttributes.getAll()).thenReturn(namingEnumeration);
        when(namingEnumeration.hasMoreElements()).thenReturn(true).thenReturn(false);
        Attribute attribute = mock(Attribute.class);
        when(namingEnumeration.nextElement()).thenReturn(attribute);

        String actual = LDAP.toString(expectedAttributes);
        assertThat(actual).startsWith(" {");
        assertThat(actual).endsWith("} ");

    }

    @Test
    void toStringAttribute() throws NamingException
    {
        assertEquals("",LDAP.toString((Attribute)null));

        Attribute expectedAttribute = mock(Attribute.class);
        assertEquals(" id=>",LDAP.toString(expectedAttribute));

        String expectedId = "nyla";
        when(expectedAttribute.getID()).thenReturn(expectedId);
        Integer expectedSize = 0;
        when(expectedAttribute.size()).thenReturn(expectedSize);
        assertEquals(" id=nyla>",LDAP.toString(expectedAttribute));

        expectedSize = 1;
        when(expectedAttribute.size()).thenReturn(expectedSize);
        String expectValue = "hello";
        when(expectedAttribute.get(0)).thenReturn(expectValue);
        String actual = LDAP.toString(expectedAttribute);
        assertThat(actual).contains("id=nyla> "+expectValue);
    }

    @Test
    void getNameFromString() throws NamingException
    {
        String idn = "hello";
        Name actual = LDAP.getNameFromString(idn);
        assertNotNull(actual);


        idn = "ldap://hello";
        actual = LDAP.getNameFromString(idn);
        assertNotNull(actual);

    }

    @Test
    void getNameFromSearchResult() throws NamingException
    {

        SearchResult idirectoryEntry = mock(SearchResult.class);
        Name iBaseDN = mock(Name.class);
        assertThrows(IllegalArgumentException.class,() -> LDAP.getNameFromSearchResult(idirectoryEntry,iBaseDN));
        String expectedName = "name";

        when(idirectoryEntry.getName()).thenReturn(expectedName);
        Name expected = mock(Name.class);
        when(iBaseDN.clone()).thenReturn(expected);
        Name actual = LDAP.getNameFromSearchResult(idirectoryEntry,iBaseDN);
        assertNotNull(actual);
        assertEquals(expected,actual);

        when(idirectoryEntry.isRelative()).thenReturn(true);
        CompoundName compoundName = mock(CompoundName.class);
        Enumeration<String> expectedAll = mock(Enumeration.class);
        when(compoundName.getAll()).thenReturn(expectedAll);
        actual = LDAP.getNameFromSearchResult(idirectoryEntry,compoundName);
        assertNotNull(actual);
    }
}
