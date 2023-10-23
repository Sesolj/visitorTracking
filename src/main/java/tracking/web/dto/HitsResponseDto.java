package tracking.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HitsResponseDto {
    private int dailyHits;
    private int totalHits;
}
