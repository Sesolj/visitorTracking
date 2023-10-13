package tracking.domain;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HitsRepositoryTest {

    @Autowired
    HitsRepository hitsRepository;

    @After
    public void cleanup() {
        hitsRepository.deleteAll();
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
