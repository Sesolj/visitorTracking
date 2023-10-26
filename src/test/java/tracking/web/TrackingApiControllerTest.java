package tracking.web;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tracking.service.TrackingService;
import tracking.web.dto.LogsRequestDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TrackingApiController.class)
public class TrackingApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TrackingService trackingService;

    @Test
    @DisplayName("url 정보 등록 API 테스트")
    public void saveUrlTest() throws Exception {
        // given
        String url = "www.test.com";

        // when, then
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/tracking/users/{url}", url)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("url 카운트 증가 API 테스트")
    public void updateHitsTest() throws Exception {
        // given
        String url = "www.test.com";

        // when, then
        mvc.perform(MockMvcRequestBuilders
                        .put("/api/tracking/hits/{url}", url)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방문자 수 조회 API 테스트")
    public void getHitsTest() throws Exception {
        // given
        String url = "www.test.com";
        trackingService.saveUrl(url);

        // when, then
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/tracking/hits-management/{url}", url)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("N일 간의 방문자 수 조회 API 테스트")
    public void getLogsTest() throws Exception {
        // given
        LogsRequestDto dto = new LogsRequestDto();
        dto.setUrl("www.test.com");
        dto.setMinDate(LocalDateTime.now().minusDays(6));
        dto.setMaxDate(LocalDateTime.now());

        // when, then
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/hits-management/statistics")
                        .characterEncoding("utf-8")
                        .flashAttr("logsRequestDto", dto))
                .andExpect(status().isOk());
    }
}
