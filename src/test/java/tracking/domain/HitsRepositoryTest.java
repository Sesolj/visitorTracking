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

    @Test
    @DisplayName("방문자 수 정보 저장하고 불러오는 테스트")
    public void getHitsTest() {
        // given
        String url = "http://test.com";
        int dailyHits = 5;
        int totalHits = 10;

        hitsRepository.save(Hits.builder()
                .url(url)
                .dailyHits(dailyHits)
                .totalHits(totalHits)
                .build());

        // when
        Hits hits = hitsRepository.findByUrl(url);

        // then
        System.out.println(">>>>>>>>>>>> getHitsTest");
        assertThat(hits.getUrl()).isEqualTo(url);
        assertThat(hits.getDailyHits()).isEqualTo(dailyHits);
        assertThat(hits.getTotalHits()).isEqualTo(totalHits);
    }

    @Test
    @DisplayName("hits 증가 테스트")
    public void addHitsTest() {
        // given
        String url = "http://test.com";
        int expectedDailyHits = 1;
        int expectedTotalHits = 1;

        hitsRepository.save(Hits.builder().url(url).build());

        // when
        Hits hits = hitsRepository.findByUrl(url);
        hits.updateHits(hits.getDailyHits(), hits.getTotalHits());

        // then
        System.out.println(">>>>>>>>>>>> addHitsTest");
        assertThat(hits.getDailyHits()).isEqualTo(expectedDailyHits);
        assertThat(hits.getTotalHits()).isEqualTo(expectedTotalHits);
    }
}
