package nyla.solutions.core.security.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SecurityRoleTest
{

	private SecurityRole subject;
	private Permission permission;
	private String permissionText = "test";

	@BeforeEach
	void setUp() {
		permission = new AllPermission();
		subject = new SecurityRole("role");
	}

	@Test
	public void checkPermission()
	{

		assertFalse(subject.checkPermission(null));
		
		assertFalse(subject.checkPermission(new SecurityPermission("")));
		subject.addPermission(new SecurityPermission(""));
		assertTrue(subject.checkPermission(new SecurityPermission("")));
		
	}

	@Test
	void getPrincipal_isNull() {
		assertThat(subject.getPrincipal()).isNull();

		assertThat(subject.getPrincipal()).isNull();
	}

	@Test
	void addPermission() {
		subject.addPermission(permission);

		assertThat(subject.getPermissions()).contains(permission);
	}
}
