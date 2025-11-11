/**
 * сущность поста в блоге
 */
package pn.back.entities;


import lombok.Data;
import lombok.ToString;
import pn.back.utils.ArrayUtils;

@Data
@ToString
public class Message {

    private long id;

    private String title;

    private String content;

    private String[] tags;

    private long likesCount;

    private String pictureUrl;

    private long commentsCount;

    public Message() {
    }


    public Message(String title, String content,
                   long likesCount, long commentsCount, String[] tags) {
        this();
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.tags = tags;
    }

    public Message(long id, String title, String content,
                   long likesCount, long commentsCount, String pictureUrl, String[] tags) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.pictureUrl = pictureUrl;
        this.tags = tags;
    }

    public Message(long id, String title, String content,
                   long likesCount, long commentsCount, String pictureUrl) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.pictureUrl = pictureUrl;
    }


    public Message(long id, String title, String content,
                   long likesCount, long commentsCount, String pictureUrl, java.sql.Array sqlTags) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.pictureUrl = pictureUrl;

        if (sqlTags != null) {
            this.tags = ArrayUtils.convertArray(sqlTags);
        }


    }


}
