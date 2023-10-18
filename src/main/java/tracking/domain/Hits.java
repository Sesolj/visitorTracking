package tracking.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Hits {

    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "daily_hits")
    private int dailyHits;

    @Column(name = "total_hits")
    private int totalHits;

    @Builder
    public Hits(String url, int dailyHits, int totalHits) {
        this.url = url;
        this.dailyHits = dailyHits;
        this.totalHits = totalHits;
    }

    /**
     * @methodName : updateHits
     * @Description: 매개변수로 받은 hits에 + 1
     **/
    public void updateHits(int dailyHits, int totalHits) {
        this.dailyHits = dailyHits + 1;
        this.totalHits = totalHits + 1;
    }
}
