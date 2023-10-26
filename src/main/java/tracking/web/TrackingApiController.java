package tracking.web;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import tracking.service.TrackingService;
import tracking.web.dto.HitsResponseDto;
import tracking.web.dto.LogsRequestDto;
import tracking.web.dto.LogsResponseDto;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController
public class TrackingApiController {

    private final TrackingService trackingService;

    /**
     * @methodName: finishDailyHits
     * @Description: 매일 자정에 작동하는 스케줄링 코드. 일일 방문자 수 저장 및 초기화 수행
     **/
    @Scheduled(cron = "0 0 0 * * ?")
    public void finishDailyHits() throws Exception {

        // 날짜와 해당 일자 방문자 수를 로깅 테이블에 저장
        trackingService.saveDateHits();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>방문자 수를 초기화합니다.");
        trackingService.resetDailyHits();
    }

    @Operation(summary = "url 정보 등록", description = "DB에 url 정보 등록하는 API")
    @PostMapping("/api/tracking/users/{url}")
    public ResponseEntity<String> save(@PathVariable("url") String url) {
        trackingService.saveUrl(url);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @Operation(summary = "url 카운트 증가", description = "url의 카운트를 증가하는 API")
    @PutMapping("/api/tracking/hits/{url}")
    public ResponseEntity<String> update(@PathVariable("url") String url) {
        trackingService.addHits(url);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @Operation(summary = "일간/누적 방문자 수 조회", description = "일간 방문자 수와 누적 방문자 수를 응답하는 API")
    @GetMapping("/api/tracking/hits-management/{url}")
    public ResponseEntity<HitsResponseDto> getHits(@PathVariable("url") String url) {
        return new ResponseEntity<>(trackingService.showHits(url), HttpStatus.OK);
    }

    @Operation(summary = "N일 간의 방문자 수 조회", description = "N일 간의 방문자 수 데이터를 응답하는 API(최대 일수: 7)")
    @GetMapping("/api/tracking/hits-management/statistics")
    public ResponseEntity<LogsResponseDto> getLogs(@ModelAttribute LogsRequestDto logsRequestDto) {
        if (logsRequestDto.getMinDate().isBefore(LocalDateTime.now().minusDays(7)))
            new Exception("7일 이상의 데이터는 조회할 수 없습니다.");

        return new ResponseEntity<>(trackingService.showStatistics(logsRequestDto), HttpStatus.OK);
    }
}
