package tracking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracking.domain.Hits;
import tracking.domain.HitsRepository;
import tracking.domain.Logs;
import tracking.domain.LogsRepository;
import tracking.exception.ApiException;
import tracking.exception.ExceptionEnum;
import tracking.web.dto.HitsResponseDto;
import tracking.web.dto.LogsRequestDto;
import tracking.web.dto.LogsResponseDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrackingService {

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
            throw new ApiException(ExceptionEnum.ALREADY_SAVED_URL);
        }
    }

    /**
     * @methodName: addHits
     * @Description: 일일 방문자 수, 전체 방문자 수 증가. url 정보가 없을 경우 새로 저장 후 방문자 수 증가시킴
     **/
    @Transactional
    public void addHits(String url) {
        Hits hits = hitsRepository.findByUrl(url);

        if (hits == null) {
            saveUrl(url);
            hits = hitsRepository.findByUrl(url);
        }

        hits.updateHits(hits.getDailyHits(), hits.getTotalHits());
    }

    /**
     * @methodName: resetDailyHits
     * @Description: 일일 방문자 수 초기화
     **/
    @Transactional
    public void resetDailyHits() {
        hitsRepository.resetAllDailyHits();
    }

    /**
     * @methodName: saveDateHits
     * @Description: 모든 hits 레코드의 일일 방문자 수를 날짜와 함께 logs 테이블에 저장
     **/
    @Transactional
    public void saveDateHits() {
        List<Hits> hitsList = hitsRepository.findAll();
        LocalDateTime realDate = LocalDateTime.now().minusDays(1);

        // [TODO] 성능 문제 없는지 확인 필요, 대용량 데이터 테스트
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
     * @Description: 방문자 수 정보 반환
     **/
    public HitsResponseDto showHits(String url) {
        Hits hits = hitsRepository.findByUrl(url);

        if (hits == null) {
            throw new ApiException(ExceptionEnum.URL_NOT_FOUND);
        }

        HitsResponseDto hitsResponseDto = new HitsResponseDto();
        hitsResponseDto.setDailyHits(hits.getDailyHits());
        hitsResponseDto.setTotalHits(hits.getTotalHits());

        return hitsResponseDto;
    }

    /**
     * @methodName: showStatistics
     * @Description: N일 간의 누적 방문자 수 정보 반환
     * @param logsRequestDto: url, 최소 일자, 최대 일자
     **/
    public LogsResponseDto showStatistics(LogsRequestDto logsRequestDto) {
        LogsResponseDto logsResponseDto = new LogsResponseDto();
        int customDaysHits = 0;

        Hits hits = hitsRepository.findByUrl(logsRequestDto.getUrl());

        if (hits == null) {
            throw new ApiException(ExceptionEnum.URL_NOT_FOUND);
        }

        List<Logs> logsList = logsRepository.findByHitsOrderByDateAsc(hits);
        if (logsList.size() > 0) {
            for (Logs l:
                 logsList) {
                // 설정 날짜 범위에 포함되는 데이터만 연산
                if(l.getDate().compareTo(logsRequestDto.getMinDate()) > 0
                        && l.getDate().compareTo(logsRequestDto.getMaxDate()) < 0)
                    customDaysHits+=l.getDateHits();
            }
        }
        else {
            // 저장된 로그 데이터가 없는 경우(트래킹 시작 후 하루가 지나지 않음), 전체 방문자 수 반환
            customDaysHits = hits.getTotalHits();
        }

        logsResponseDto.setCustomDaysHits(customDaysHits);

        return logsResponseDto;
    }
}
