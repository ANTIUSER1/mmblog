package pn.back.repositories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pn.back.entities.Message;


import java.util.List;

@Repository
@Transactional
public interface MessageRepository extends CrudRepository<Message, Long> {


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
}
