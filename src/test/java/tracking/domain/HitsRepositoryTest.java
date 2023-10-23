package tracking.domain;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HitsRepositoryTest {

    @Autowired
    HitsRepository hitsRepository;

    @Autowired
    EntityManager entityManager; // 영속성 컨텍스트 관리

    @Test
    @DisplayName("방문자 수 정보 불러오는 테스트")
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
    @DisplayName("방문자 수 증가 테스트")
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

    @Test
    @DisplayName("벌크 업데이트 테스트-모든 레코드의 일간 방문자 수 초기화")
    public void resetAllDailyHitsTest() {
        // given
        String url = "http://test.com";
        int dailyHits = 5;
        int resetHits = 0;

        hitsRepository.save(Hits.builder()
                .url(url)
                .dailyHits(dailyHits)
                .build());

        // when
        hitsRepository.resetAllDailyHits();

        entityManager.flush();
        entityManager.clear(); // 영속성 컨텍스트 초기화

        Hits hits = hitsRepository.findByUrl(url);

        // then
        System.out.println(">>>>>>>>>>>> resetAllDailyHitsTest");
        assertThat(hits.getDailyHits()).isEqualTo(resetHits);
    }
}
