package tracking.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LogsRepository extends JpaRepository<Logs, Long> {
    List<Logs> findByHitsOrderByDateAsc(Hits hits);
    List<Logs> findAllByOrderByDateDesc();

    // [TODO] 에러 원인 확인, 로우 쿼리외의 적합한 조회 방식 고민 필요
//    @Query(value = "SELECT sum(dateHits) FROM Logs l " +
//            "WHERE DATE(l.date) >= :min_date " +
//            "AND DATE(l.date) <= :max_date " +
//            "GROUP BY l.hits " +
//            "HAVING l.hits = :url_id)",
//            nativeQuery = true)
//    int sumCustomDaysHits(@Param(value = "url_id") Hits hits, @Param(value = "min_date") LocalDateTime minDate,
//                          @Param(value = "max_date")LocalDateTime maxDate);
}
