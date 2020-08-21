package uk.ac.ebi.ep.restapi.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author joseph
 */
@Getter
@Setter
@Builder
public class ExceptionDescriptor {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    private String description;
}
