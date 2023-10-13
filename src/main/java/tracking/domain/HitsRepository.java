package tracking.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HitsRepository extends JpaRepository<Hits, Long> {
    Hits findByUrl(String url);

    @Modifying
    @Query("update Hits h set h.dailyHits = 0")
    void resetAllDailyHits();
}
