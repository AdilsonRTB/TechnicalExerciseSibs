package cv.pn.sibs.utilities;

import lombok.*;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class APIResponse {

    private boolean status;
    private String statusText;
    private List<Object> details;

}
