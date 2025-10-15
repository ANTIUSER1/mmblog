/**
 * сервис работы с сообщениями
 */
package pn.back.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pn.back.entities.Message;
import pn.back.entities.MessagePageData;
import pn.back.repo.MessageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessageService {

    public static final String FILE_PREFIX = "/usr/local/tomcat/webapps/blog/img/m-";

    @Autowired
    private String applicationName;
    @Autowired
    private MessageRepository messageRepository;


    public List<Message> findAll() {
        log.info(" Process for showing all  messages");
        return messageRepository.findAll();
    }


    public MessagePageData showAllPG(int page, int limit, String search) {
        log.info(" Process for showing   messages by creteria title or content has string {} page {} line to {} line",
                search, page, page + limit
        );
        List<Message> res = messageRepository.showAllByPage(
                page * limit, limit, search
        );
        long total = messageRepository.numberOfRecords(search);
        long last = 0;
        if (total == 0) {
            res = new ArrayList<>();
            page = 0;
            last = 0;
        } else {
            last = total / limit;
        }
        return new MessagePageData(res, page < last, page > 0, last);
    }


    public Message modifyMessage(Message message, long id) throws SQLException {
        if (id == message.getId() || message.getId() == 0) {
            log.info("\n\t Process for modifiing  message od id {} \n by value {} ",
                    id, message
            );
            if (message.getContent() != null && message.getTitle() != null) {
                log.info("\n\nProcess of UPDATE content,   title \n");
                return messageRepository.updateContentTitle(id,
                        message.getContent(), message.getTitle());
            } else if (message.getContent() != null && message.getTitle() == null) {
                log.info("Process of UPDATE content ");
                return messageRepository.updateContent(id, message.getContent());
            } else if (message.getContent() == null && message.getTitle() != null) {
                log.info("Process of UPDATE title ");
                return messageRepository.updateContentTitle(id, message.getContent(), message.getTitle());
            }
        } else {
            log.info("\n Incorrect request for Message: Expected ID {}; given ID {}", message.getId(), id);
        }
        return null;
    }

    public Optional<Message> incrementLikes(long id) {
        log.info(" Process for increment likes  message of {} ", id);
        Message message = messageRepository.findById(id);
        if (message != null) {
            long likes = message.getLikesCount() + 1;
            message.setLikesCount(likes);
            return Optional.of(messageRepository.incrementLikes(id, likes));

        } else return Optional.empty();
    }


    public Optional<Message> addMessage(Message message) {
        log.info(" Process for adding  message  ");
        Message messageSaved = messageRepository.save(message);
        if ((messageSaved != null)) return Optional.of(messageSaved);
        return Optional.empty();
    }

    public long delete(long id) {
        log.info(" Process for deleting  message of {} ", id);
        return messageRepository.delete(id);
    }


    public Optional<String> getPicture(long id) {
        Message message = messageRepository.findById(id);
        if (message != null) return Optional.of(message.getPictureUrl());
        return Optional.empty();
    }

    public Optional<Message> addPicture(MultipartFile file, long id) throws IOException {
        Message m = findById(id);
        System.out.println("\n \n MSG :: " + m);
        if (findById(id) == null) {
            log.info("\n No such message with ID {}", id);
            return Optional.empty();
        }

        byte[] fbytes = file.getBytes();
        log.info("Process for updating picture for message {} ", id);
        // Message fromDB = messageRepository.findById(id).get();
        String pictureUrl = uploadFile(file, id);
        // fromDB.setPictureUrl(pictureUrl);
        if (messageRepository.addPicture(id, pictureUrl))
            return Optional.of(messageRepository.findById(id));
        else return Optional.empty();
    }

    //*********
    private String uploadFile(MultipartFile file, long id) throws IOException {
        String[] fileNameParts = file.getOriginalFilename().split("\\.");
        String pictureAddress = createPictureAddress(id) + fileNameParts[1].toLowerCase();
        log.info("\n Picture will be copied into {}", pictureAddress);

        File copied = new File(pictureAddress);
        File dir = copied.getParentFile();
        if (!dir.exists()) dir.mkdirs();
        String[] pictureUrlParts = pictureAddress.split("/");
        String pictureUrl = "/" + applicationName + "/img/" + pictureUrlParts[pictureUrlParts.length - 1];
        System.out.println("\n  \t URL\n " + pictureUrl);
        byte[] fbytes = file.getBytes();
        Files.write(copied.toPath(), fbytes);
        log.info("\n Picture will be accesible at {}", pictureUrl);

        return pictureUrl;
    }


    private String createPictureAddress(long id) {
        return FILE_PREFIX + id + ".";
    }

    public Optional<Message> incrementComments(long id) {
        log.info(" Process for increment Comment count  message of {} ", id);
        Message message = messageRepository.findById(id);
        if (message != null) {
            System.out.println("\n\tMSG----  :: \n " + message);
            long countComments = message.getCommentsCount() + 1;
            message.setCommentsCount(countComments);
            message = messageRepository.incrementCommentsCount(message);
            System.out.println("\nCOUNT UPDATE\n " + message);
            return Optional.of(message);

        } else return Optional.empty();

    }

    public Message findById(long id) {
        return messageRepository.findById(id);
    }
//
//
//    public Optional<Message> showById(long id) {
//        log.info(" Process for showing   message with id {} ", id);
//        return messageRepository.findById(id);
//    }
//
//
//    public void delete(long id) {
//        log.info(" Process for deleting  message of {} ", id);
//        Optional<Message> optionalMessage = messageRepository.findById(id);
//        if (optionalMessage.isPresent()) {
//            Message message = optionalMessage.get();
//            messageRepository.delete(message);
//        }
//    }
//
//    public Optional<Message> incrementLikes(long id) {
//        log.info(" Process for increment likes  message of {} ", id);
//        Optional<Message> optionalMessage = messageRepository.findById(id);
//        if (optionalMessage.isPresent()) {
//            Message message = optionalMessage.get();
//            long likes = message.getLikesCount() + 1;
//            message.setLikesCount(likes);
//            messageRepository.incrementLikes(id, likes);
//            return Optional.of(message);
//        } else return Optional.empty();
//    }
//
////    public Message addPicture(MultipartFile file, long id) throws IOException {
////        byte[] fbytes = file.getBytes();
////        log.info("Process for updating picture for message {} ", id);
////
////        // Message fromDB = messageRepository.findById(id).get();
////        String pictureUrl = uploadFile(file, id);
////        // fromDB.setPictureUrl(pictureUrl);
////        messageRepository.addPicture(id, pictureUrl);
////        return messageRepository.findById(id).get();
////    }
////
////    private String uploadFile(MultipartFile file, long id) throws IOException {
////        String[] fileNameParts = file.getOriginalFilename().split("\\.");
////        String pictureAddress = createPictureAddress(id) + fileNameParts[1].toLowerCase();
////        log.info("\n Picture will be copied into {}", pictureAddress);
////
////        File copied = new File(pictureAddress);
////        File dir = copied.getParentFile();
////        if (!dir.exists()) dir.mkdirs();
////        String[] pictureUrlParts = pictureAddress.split("/");
////        String pictureUrl = "/blog/img/" + pictureUrlParts[pictureUrlParts.length - 1];
////
////        byte[] fbytes = file.getBytes();
////        Files.write(copied.toPath(), fbytes);
////        log.info("\n Picture will be accesible at {}", pictureUrl);
////
////        return pictureUrl;
////        //ResponseEntity.ok().body("file received successfully");
////    }
//
//    private String createPictureAddress(long id) {
//        return FILE_PREFIX + id + ".";
//    }

//    public String getPicture(long id) {
//        return messageRepository.findById(id).get().getPictureUrl();
//    }

//    public MessagePageData showAllPG(int page, int limit, String search) {
//        log.info(" Process for showing   messages by creteria title or content has string {} page {} line to {} line",
//                search, page, page + limit
//        );
//        List<Message> res = messageRepository.showAllByPage(
//                page * limit, limit, search
//        );

//        long total = messageRepository.numberOfRecords(search);
//        long last = 0;
//        if (total == 0) {
//            res = new ArrayList<>();
//            page = 0;
//            last = 0;
//        } else {
//            last = total / limit;
//        }
//        return new MessagePageData(res, page < last, page > 0, last);
//    }
}
