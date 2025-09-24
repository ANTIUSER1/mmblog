/**
 * класс сущности поста в блоге
 */
package pn.back.entities;


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


    private String[] tags;

    private long likesCount;

    private String pictureUrl;



    // @MappedCollection(keyColumn = "id", idColumn = "message_key")
    private List<Comment> commentList;


    public int getCommentCount() {
        return commentList.size();
    }


}
