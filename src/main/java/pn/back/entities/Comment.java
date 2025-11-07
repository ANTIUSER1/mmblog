/**
 * сущность комментария
 */
package pn.back.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    private long id;

    private String content;
    private long messageKey;


    public Comment(String content) {
        this.content = content;
        this.messageKey = messageKey;
        this.messageKey = messageKey;
    }
}
