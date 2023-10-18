package tracking.web;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import tracking.domain.Hits;
import tracking.service.TrackingService;
import tracking.web.dto.HitsResponseDto;
import tracking.web.dto.LogsResponseDto;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController
public class TrackingApiController {

    private final TrackingService trackingService;

    // Q 컨트롤러에서 스케줄 기능 수행해도 될까?
    // 스케줄링
    @Scheduled(cron = "0 22 2 * * ?") // 매일 정각에 조회수 초기화
    public void finishDailyHits() throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>조회수를 초기화합니다.");

        // 날짜와 해당 일자 조회수를 로깅 테이블에 저장
        trackingService.saveDateHits();

        // 모든 컬럼의 dailyHits 0으로 초기화
        trackingService.resetDailyHits();
    }

    // Q 예외 처리
    // DB에 url 정보 등록하는 API
    @PostMapping("/api/tracking")
    public String save(String url) {
        trackingService.saveUrl(url);
        return "OK";
    }

    // url의 카운트를 증가하는 API
    @PutMapping("/api/tracking/{url}")
    public String update(String url) {
        trackingService.addHits(url);
        return  "OK";
    }

    // 일간 조회수와 누적 조회수를 응답하는 API
    @GetMapping("/api/tracking/get-hits/{url}")
    public HitsResponseDto getHits(String url) {
        return trackingService.showHits(url);
    }

    // N일 간의 조회수 통계 데이터를 응답하는 API(기본값: 7, 최대값: 7)
    @GetMapping("/api/tracking/get-hits-statistic/{url}")
    public LogsResponseDto getHits(String url, LocalDateTime minDate, LocalDateTime maxDate) {
        return trackingService.showStatistics(url, minDate, maxDate);
    }
}
