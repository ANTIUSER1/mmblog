package pn.back.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@ToString
@Table("messages")
public class Comment {

    @Id
    private long id;

    private String content;
    private long messageKey;

}
