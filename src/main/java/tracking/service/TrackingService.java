package tracking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracking.domain.Hits;
import tracking.domain.HitsRepository;
import tracking.domain.Logs;
import tracking.domain.LogsRepository;
import tracking.web.dto.HitsResponseDto;
import tracking.web.dto.LogsResponseDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrackingService {

    // Q private final 필요한 이유
    private final HitsRepository hitsRepository;
    private final LogsRepository logsRepository;

    /**
     * @methodName: saveUrl
     * @Description: url 정보 저장
     **/
    @Transactional
    public void saveUrl(String url) {
        Hits hits = hitsRepository.findByUrl(url);

        if (hits == null) {
            hitsRepository.save(Hits.builder()
                    .url(url)
                    .build());
        } else {
            // 이미 등록된 url입니다.
        }
    }

    /**
     * @methodName: addHits
     * @Description: 일일 조회수, 전체 조회수 증가
     **/
    @Transactional
    public void addHits(String url) {
        Hits hits = hitsRepository.findByUrl(url);

        if (hits == null) {
            new IllegalArgumentException("");
        } else  {
            hits.updateHits(hits.getDailyHits(), hits.getTotalHits());
        }
    }

    /**
     * @methodName: resetDailyHits
     * @Description: 일일 조회수 초기화
     **/
    @Transactional
    public void resetDailyHits() {
        // 모든 daily_hits column 0으로 초기화
        hitsRepository.resetAllDailyHits();
    }

    /**
     * @methodName: saveDateHits
     * @Description: 일일 조회수를 날짜와 함께 저장
     **/
    @Transactional
    public void saveDateHits() {
        List<Hits> hitsList = hitsRepository.findAll();
        LocalDateTime realDate = LocalDateTime.now().minusDays(1);

        // Q 성능 문제
        for (Hits h : hitsList) {
            // 날짜별로 정렬 후 데이터 가져오기
            List<Logs> logsList = logsRepository.findByHitsOrderByDateAsc(h);

            if (!logsList.isEmpty()) {
                Logs oldest = logsList.get(0);
                LocalDateTime dateLimit = LocalDateTime.now().minusDays(7);

                // 가장 오래된 레코드 날짜가 7일 이상일 경우 update
                if (oldest.getDate().isBefore(dateLimit)) {
                    oldest.update(realDate, h.getDailyHits());
                    break;
                }
            }

            logsRepository.save(Logs.builder()
                    .hits(h)
                    .dateHits(h.getDailyHits())
                    .date(realDate)
                    .build());
        }
    }

    /**
     * @methodName: showHits
     * @Description: 조회수 정보 반환
     **/
    public HitsResponseDto showHits(String url) {
        Hits hits = hitsRepository.findByUrl(url);

        HitsResponseDto hitsResponseDto = new HitsResponseDto();
        hitsResponseDto.dailyHits = hits.getDailyHits();
        hitsResponseDto.totalHits = hits.getTotalHits();

        return hitsResponseDto;
    }

    /**
     * @methodName: showStatistics
     * @Description: N일 간의 누적 Hits 정보 반환
     * @param minDate: 조회하고자 하는 기간의 최소 일자
     * @param maxDate: 조회하고자 하는 기간의 최대 일자
     **/
    // 예외 처리: 설정한 날짜 범위의 데이터가 없을 경우 고려
    public LogsResponseDto showStatistics(String url, LocalDateTime minDate, LocalDateTime maxDate) {
        LogsResponseDto logsResponseDto = new LogsResponseDto();

        // 양방향 매핑하면 불필요한 과정일듯
        Hits hits = hitsRepository.findByUrl(url);
        List<Logs> logsList = logsRepository.findByHitsOrderByDateAsc(hits);



        return logsResponseDto;
    }
}
