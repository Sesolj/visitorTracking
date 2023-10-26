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
import java.time.format.DateTimeFormatter;
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
    @DisplayName("7일 이상된 로그를 최신 데이터로 업데이트하는 테스트")
    public void updateOldestLogs() {
        // given
        Hits oldHits  = Hits.builder().url("http://www.test.com").dailyHits(1).build();
        Hits newHits = Hits.builder().url("http://www.test.com").dailyHits(2).build();
        LocalDateTime oldDate = LocalDateTime.now().minusDays(7);

        hitsRepository.save(oldHits);

        logsRepository.save(Logs.builder()
                .hits(oldHits)
                .dateHits(oldHits.getDailyHits())
                .date(oldDate)
                .build());

        // when
        List<Logs> logsList = logsRepository.findByHitsOrderByDateAsc(oldHits);
        Logs oldest = logsList.get(0);
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(7);

        // 가장 오래된 레코드 날짜가 7일 이상일 경우 update
        if (oldest.getDate().isBefore(dateLimit)) {
            oldest.update(LocalDateTime.now(), newHits.getDailyHits());
        }

        // then
        assertThat(oldest.getDate().format(DateTimeFormatter.ISO_DATE)).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
    }

    @Test
    @DisplayName("N일 간의 방문자 수 불러오는 테스트")
    public void showStatistics() {
        // given
        int dailyHits = 5;
        Hits hits = new Hits("http://test.com", dailyHits, 10);
        int days = 7;
        LocalDateTime minDate = LocalDateTime.now().minusDays(days);
        LocalDateTime maxDate = LocalDateTime.now();

        int returnedHits = 0;
        int expectedSumHits = dailyHits*days;

        hitsRepository.save(Hits.builder()
                .url(hits.getUrl())
                .dailyHits(hits.getDailyHits())
                .totalHits(hits.getTotalHits())
                .build());

        // 테스트용 일일 로그 데이터 생성
        for(int i = 1;i <= days;i++){
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