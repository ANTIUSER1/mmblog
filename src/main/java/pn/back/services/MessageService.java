package pn.back.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pn.back.entities.Message;

import pn.back.repo.MessageRepository;
import pn.back.repo.MessageRepositoryImpl;


import java.util.List;

@Service
@Slf4j
public class MessageService {

    public static final String FILE_PREFIX = "/usr/local/tomcat/webapps/blog/img/m-";



    @Autowired
    private MessageRepository messageRepository;


    public List<Message> findAll() {
        log.info(" Process for showing all  messages");
        return messageRepository.findAll();
    }
//
//    public Optional<Message> modifyMessage(Message message, long id) {
//        log.info(" Process for modifiing  message od id {} \n by value {} ",
//                id, message
//        );
//        if (message.getContent() != null && message.getTitle() != null) {
//            log.info("Process of UPDATE content,   title");
//            messageRepository.updateContentTitle(id,
//                    message.getContent(), message.getTitle());
//
//        } else if (message.getContent() != null && message.getTitle() == null) {
//            log.info("Process of UPDATE content ");
//            messageRepository.updateContent(id, message.getContent());
//
//        } else if (message.getContent() == null && message.getTitle() != null) {
//            log.info("Process of UPDATE title ");
//            messageRepository.updateTitle(id, message.getTitle());
//        }
//        return messageRepository.findById(id);
//    }
//
//    public Optional<Message> addMessage(Message message) {
//        log.info(" Process for adding  message  ");
//        return Optional.of(messageRepository.save(message));
//    }
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
