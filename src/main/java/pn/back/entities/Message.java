/**
 * класс сущности поста в блоге
 */
package pn.back.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Table("messages")
public class Message implements Serializable {

    @Id
    private long id;

    private String title;

    private String content;

    @JsonIgnore
    private String[] tags;

    private long likesCount;

    private String pictureUrl;

    @MappedCollection(keyColumn = "id", idColumn = "message_key")
    @JsonIgnore
    private List<Comment> commentList;

    public Message() {
        commentList = new ArrayList<>();
    }

    public Message(long id, String title, String content, long likesCount, String pictureUrl) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.pictureUrl = pictureUrl;
    }

    public int getCommentCount() {
        return commentList.size();
    }


}
