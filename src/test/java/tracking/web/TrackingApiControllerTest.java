package tracking.web;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import tracking.domain.HitsRepository;
import tracking.domain.LogsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrackingApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HitsRepository hitsRepository;

    @Autowired
    private LogsRepository logsRepository;

    @After
    public void tearDown() throws Exception {
        hitsRepository.deleteAll();
        logsRepository.deleteAll();
    }

    @Test
    public void saveUrlTest() throws Exception {

    }

    @Test
    public void updateHitsTest() throws Exception {

    }
}
