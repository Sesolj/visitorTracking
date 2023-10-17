package tracking.domain;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HitsRepositoryTest {

    @Autowired
    HitsRepository hitsRepository;

    }

    @Test
    public void getHits() {
        String url = "http://test.com";

        hitsRepository.save(Hits.builder().url(url).build());

        List<Hits> hitsList = hitsRepository.findAll();

        Hits hits = hitsList.get(0);
        System.out.println(hits.getUrl());
        assertThat(hits.getUrl()).isEqualTo(url);
    }
}
