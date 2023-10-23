package tracking.domain;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LogsRepositoryTest {
    @Autowired
    LogsRepository logsRepository;

    @Test
    @DisplayName("logs 데이터 불러오는 테스트")
    public void getLogsTest() {
        // given
        LocalDateTime date = LocalDateTime.now();
        String dateFormat = date.format(DateTimeFormatter.ISO_DATE);

        logsRepository.save(Logs.builder().date(date).build());

        // when
        List<Logs> logsList = logsRepository.findAllByOrderByDateDesc();

        // then
        Logs logs = logsList.get(0);
        System.out.println("//////////////test////" + logs.getDate().format(DateTimeFormatter.ISO_DATE));

        assertThat(logs.getDate().format(DateTimeFormatter.ISO_DATE)).isEqualTo(dateFormat);
    }
}
