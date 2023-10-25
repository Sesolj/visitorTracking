package tracking.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HitsResponseDto {
    private int dailyHits;
    private int totalHits;
}
