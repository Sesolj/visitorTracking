package tracking.domain;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogsRepositoryTest {
    // Q 외래키 테스트 코드??
    @Autowired
    LogsRepository logsRepository;

    @After
    public void cleanup() {
        logsRepository.deleteAll();
    }

    @Test
    public void getLogs() {
        LocalDateTime date = LocalDateTime.now();
        String dateFormat = date.format(DateTimeFormatter.ISO_DATE);

        logsRepository.save(Logs.builder().date(date).build());

        List<Logs> logsList = logsRepository.findAll();

        Logs logs = logsList.get(0);
        System.out.println("//////////////test////" + logs.getDate().format(DateTimeFormatter.ISO_DATE));

        assertThat(logs.getDate().format(DateTimeFormatter.ISO_DATE)).isEqualTo(dateFormat);
    }
}
