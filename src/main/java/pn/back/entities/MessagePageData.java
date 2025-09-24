package pn.back.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessagePageData {

    private List<Message> posts;
    private boolean hasNext;
    private boolean hasPrev;
    private long lastPage;

}
