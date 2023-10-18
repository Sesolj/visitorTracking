package tracking.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Logs, Long> {
    List<Logs> findByHitsOrderByDateAsc(Hits hits);
}
