package tracking.web;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
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
    @Scheduled(cron = "0 0 0 * * ?") // 매일 정각에 조회수 초기화
    public void finishDailyHits() throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>조회수를 초기화합니다.");

        // 날짜와 해당 일자 조회수를 로깅 테이블에 저장
        trackingService.saveDateHits();

        // 모든 컬럼의 dailyHits 0으로 초기화
        trackingService.resetDailyHits();
    }

    // [TODO] API Response 구체화
    // [TODO] API 에러 반환 처리
    @Operation(summary = "url 정보 등록", description = "DB에 url 정보 등록하는 API")
    @PostMapping("/api/tracking/users/{url}")
    public String save(@RequestParam String url) {
        trackingService.saveUrl(url);
        return "OK";
    }

    @Operation(summary = "url 카운트 증가", description = "url의 카운트를 증가하는 API")
    @PutMapping("/api/tracking/hits/{url}")
    public String update(@RequestParam String url) {
        trackingService.addHits(url);
        return  "OK";
    }

    @Operation(summary = "일간/누적 hits 조회", description = "일간 조회수와 누적 조회수를 응답하는 API")
    @GetMapping("/api/tracking/hits-management/{url}")
    public HitsResponseDto getHits(@RequestParam String url) {
        return trackingService.showHits(url);
    }

    @Operation(summary = "N일 간의 hits 조회", description = "N일 간의 조회수 통계 데이터를 응답하는 API(최대 일수: 7)")
    @Operation(summary = "N일 간의 hits 조회", description = "N일 간의 조회수 데이터를 응답하는 API(최대 일수: 7)")
    @GetMapping("/api/tracking/hits-management/statistics/{url}")
        return trackingService.showStatistics(url, minDate, maxDate);
    }
}
