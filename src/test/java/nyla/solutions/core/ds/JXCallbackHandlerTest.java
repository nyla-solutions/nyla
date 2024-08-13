package nyla.solutions.core.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JXCallbackHandlerTest {

    private JXCallbackHandler subject;

    @BeforeEach
    void setUp() {
        subject = new JXCallbackHandler();
    }

    @Test
    void handle_nameCallBack_SetName() throws IOException, UnsupportedCallbackException {
        NameCallback nameCallback = mock(NameCallback.class);
        Callback[] callbacks = {nameCallback};

        subject.handle(callbacks);

        verify(nameCallback).setName(anyString());
    }
}