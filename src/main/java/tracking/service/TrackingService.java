package tracking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracking.domain.Hits;
import tracking.domain.HitsRepository;
import tracking.domain.Logs;
import tracking.domain.LogsRepository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrackingService {

    // Q private final 필요한 이유
    private final HitsRepository hitsRepository;
    private final LogsRepository logsRepository;

    /**
     * @methodName : saveUrl
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
     * @methodName : addHits
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
     * @methodName : resetDailyHits
     * @Description: 일일 조회수 초기화
     **/
    @Transactional
    public void resetDailyHits() {
        // 모든 daily_hits column 0으로 초기화
        hitsRepository.resetAllDailyHits();
    }

    public void saveDateHits() {
        // 일자 별 조회수 저장
        // 가장 오래된 레코드가 7일 이상 전이면 삭제 후 해당 데이터 삽입
    }

    public void showDailyHits() {
        // 일간 조회수 반환
    }

    public void showTotalHits() {
        // 전체 조회수 반환
    }

    // @param: minDate, maxDate
    public void showStatistics() {
        // N일 간의 누적 조회수 반환
        // 예외 처리: 설정한 날짜 범위의 데이터가 없을 경우 고려
    }
}
