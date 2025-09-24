/**
 * класс сущности поста в блоге
 */
package pn.ablog.back.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
//@Table("messages")
public class Message {

    //    @Id
    private long id;

    private String title;

    private String content;

    @JsonIgnore
    private String[] tags;

    private long likesCount;

    private String pictureUrl;


    @JsonIgnore
    // @MappedCollection(keyColumn = "id", idColumn = "message_key")
    private List<Comment> commentList;


    public int getCommentCount() {
        return commentList.size();
    }


}
