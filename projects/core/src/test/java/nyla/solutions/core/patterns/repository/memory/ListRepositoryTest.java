package nyla.solutions.core.patterns.repository.memory;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ListRepositoryTest {

    private final UserProfile user1 = JavaBeanGeneratorCreator.of(UserProfile.class).create();

    @Test
    void saveList() {

        ListRepository<UserProfile> subject = new ListRepository<>(new ArrayList<UserProfile>());
        subject.save(user1);

        assertThat(subject.findAll()).isNotEmpty();
    }

    @Test
    void saveDuplicateList() {

        ListRepository<UserProfile> subject = new ListRepository<>(new ArrayList<UserProfile>(),true);
        subject.save(user1);
        subject.save(user1);

        int cnt=0;
        for( var i : subject.findAll())
            cnt++;

        assertThat(cnt).isEqualTo(1);
    }
}