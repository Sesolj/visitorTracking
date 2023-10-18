package tracking.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Column(name = "date_hits")
    private int dateHits;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="url_id")
    private Hits hits;

    @Builder
    public Logs(LocalDateTime date, int dateHits, Hits hits) {
        this.date = date;
        this.dateHits = dateHits;
        this.hits = hits;
    }

    public void update(LocalDateTime date, int dateHits) {
        this.date = date;
        this.dateHits = dateHits;
    }
}
