package pn.back.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
//@Table("comments")
public class Comment {

    //    @Id
    private long id;

    private String content;
    private long messageKey;
}
