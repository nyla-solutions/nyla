package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AllPermissionTest
{

    private final AllPermission subject = new AllPermission();

    @Test
	public void testIsAuthorized()
	{
		assertTrue(new AllPermission().isAuthorized(new SecurityPermission("TEST")));
		assertTrue(new AllPermission().isAuthorized( new SecurityPermissionContains("TEST")));
		assertTrue(new AllPermission().isAuthorized(new SecurityPermission("ALL")));
		assertTrue(new AllPermission().isAuthorized(new AllPermission()));
		
		assertFalse(new SecurityPermissionContains("All").isAuthorized(new AllPermission()));

	}


    @Test
    void getText() {

        assertThat(subject.getText()).isEqualTo("ALL");
    }

    @Test
    void verifyEquals() {

        assertThat(subject).isEqualTo(new AllPermission());
    }

    @Test
    void verifyHasCode() {

        assertThat(subject.hashCode()).isEqualTo(new AllPermission().hashCode());
    }
}
