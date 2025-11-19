package nyla.solutions.core.patterns.repository.memory;

import nyla.solutions.core.exception.AccessErrorException;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryRepositoryTest {

    private final static UserProfile userProfile = JavaBeanGeneratorCreator.of(UserProfile.class).create();
    private InMemoryRepository<UserProfile,String> subject;
    private Map<String, UserProfile> map;

    @BeforeEach
    void setUp() {
        subject = InMemoryRepository.builder().withIdProperty("email").build();
    }

    @Test
    void readWrite() {
        subject.save(userProfile);

        assertThat(subject.findById(userProfile.getEmail())).isEqualTo(Optional.of(userProfile));
    }

    record UserRecordPrivate(String id, String name){};
    public record UserRecord(String id, String name){};

    @Test
    void saveGetRecordPrivate() {

        var user1 = new UserRecordPrivate("1","name");
        var subject =  InMemoryRepository.builder().withIdProperty("id").build();

        Assertions.assertThrows(AccessErrorException.class, ()-> subject.save(user1));

    }

    @Test
    void saveGetRecord() {

        var user1 = new UserRecord("1","name");
        var subject =  InMemoryRepository.builder().withIdProperty("id").build();

        subject.save(user1);

        assertThat(subject.findById(user1.id).get()).isEqualTo(user1);
    }
}