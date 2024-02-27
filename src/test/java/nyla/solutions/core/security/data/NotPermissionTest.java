package nyla.solutions.core.security.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotPermissionTest {


    @Test
    void isAuthorized() {
        var permission = mock(Permission.class);

        when(permission.isAuthorized(any())).thenReturn(true);
        var subject = new NotPermission(permission);

        assertThat(subject.isAuthorized(new AllPermission())).isEqualTo(false);
    }

    @Test
    void getText() {
        var text = "permission";
        var expected = "!"+text;
        var permission = mock(Permission.class);
        when(permission.getText()).thenReturn(text);
        var subject = new NotPermission(permission);


        var actual = subject.getText();

        assertThat(actual).isEqualTo(expected);
    }
}