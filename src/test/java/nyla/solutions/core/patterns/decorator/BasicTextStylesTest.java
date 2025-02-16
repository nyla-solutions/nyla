package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Debugger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BasicTextStylesTest {

    private BasicTextStyles subject;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        subject = new BasicTextStyles();
        userProfile = JavaBeanGeneratorCreator.of(UserProfile.class).create();
    }

    @Test
    void format_withObject() {
        var text = "Hi ${lastName}";

        var actual = subject.format(text,userProfile);
        Debugger.println("actual: "+actual);

        assertThat(actual).isEqualTo("Hi "+userProfile.getLastName());
    }
}