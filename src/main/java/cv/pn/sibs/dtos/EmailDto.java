package cv.pn.sibs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    String recipient;
    String msgBody;
    String subject;
    String attachment;
}
