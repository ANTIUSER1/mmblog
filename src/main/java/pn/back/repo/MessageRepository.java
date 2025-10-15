/**
 * контракт работы с сщщбщениями
 */
package pn.back.repo;

import pn.back.entities.Comment;
import pn.back.entities.Message;

import java.sql.SQLException;
import java.util.List;


public interface MessageRepository {

    Message findById(long id);

    List<Message> findAll();

    long numberOfRecords(String search);

    List<Message> showAllByPage(int page, int limit, String search);

    Message updateContentTitle(long id, String content, String title);

    Message updateContent(long id, String content) throws SQLException;

    Message updateTitle(long id, String title);

    Message incrementCommentsCount(Message message);

    Message incrementLikes(long id, long likes);

    Message save(Message message);

    long delete(long id);

    boolean addPicture(long id, String pictureUrl);


    List<Comment> commentsForMessage(Message message);


/*
    @Query("select count(id) from blog.messages")
    long numberOfRecords();

    @Query(" SELECT COUNT(id) FROM blog.messages m WHERE  " +
            "  m.title like concat('%', :search,'%') " +
            " OR   m.content like concat('%', :search,'%') ; ")
    long numberOfRecords(String search);

    @Query("SELECT * FROM blog.messages m ORDER BY m.id asc")
    List<Message> showAll();

    @Query(" SELECT * FROM blog.messages m   WHERE  " +
            " m.title like concat('%', :search,'%') " +
            " OR  m.content like concat('%', :search,'%') " +
            "     order by m.id asc" +
            "    offset :page limit :limit ")
    List<Message> showAllByPage(int page, int limit, String search);


    @Modifying
    @Query(
            " UPDATE blog.messages  m" +
                    "         SET " +
                    "            content = :content ," +
                    "           title = :title  " +
                    "           WHERE m.id = :id   "
    )
    void updateContentTitle(long id, String content, String title);

    @Modifying
    @Query(
            "  UPDATE blog.messages m" +
                    "         SET" +
                    "             content = :content  " +
                    "            WHERE m.id = :id "
    )
    void updateContent(long id, String content);


    @Modifying
    @Query(
            " UPDATE blog.messages m" +
                    "       SET" +
                    "           title = :title " +
                    "          WHERE m.id = :id "
    )
    void updateTitle(long id, String title);


    @Modifying
    @Query(
            "   UPDATE blog.messages m" +
                    "        SET" +
                    "            picture_url = :pictureUrl " +
                    "           WHERE m.id = :id"
    )
    void addPicture(long id, String pictureUrl);


    @Modifying
    @Query(
            "       UPDATE blog.messages m " +
                    "        SET" +
                    "            likes_count = :likes" +
                    "           WHERE m.id = :id"
    )
    void incrementLikes(long id, long likes);

 */
}
