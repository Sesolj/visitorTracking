package tracking.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import tracking.domain.Hits;
import tracking.domain.HitsRepository;
import tracking.domain.Logs;
import tracking.domain.LogsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrackingServiceTest {

    @Autowired
    HitsRepository hitsRepository;

    @Autowired
    LogsRepository logsRepository;

    @Test
    @DisplayName("7일 간의 조회수 불러오는 테스트")
    public void showStatistics() {
        // given
        Hits hits = new Hits("http://test.com", 5, 10);
        LocalDateTime minDate = LocalDateTime.now().minusDays(7);
        LocalDateTime maxDate = LocalDateTime.now();

        int returnedHits = 0;
        int expectedSumHits = 35;

        hitsRepository.save(Hits.builder()
                .url(hits.getUrl())
                .dailyHits(hits.getDailyHits())
                .totalHits(hits.getTotalHits())
                .build());

        for(int i = 1;i <= 7;i++){
            logsRepository.save(Logs.builder()
                    .hits(hits)
                    .dateHits(hits.getDailyHits())
                    .date(LocalDateTime.now().minusDays(i))
                    .build());
        }

        // when
        List<Logs> logsList = logsRepository.findByHitsOrderByDateAsc(hits);
        if (logsList.size() > 0) {
            for (Logs l:
                    logsList) {
                if(l.getDate().compareTo(minDate) > 0 && l.getDate().compareTo(maxDate) < 0)
                    returnedHits+=l.getDateHits();
            }
        }

        // then
        assertThat(returnedHits).isEqualTo(expectedSumHits);
    }
}