package song1.chatting.repository;

import org.springframework.stereotype.Repository;
import song1.chatting.domain.ChatData;

@Repository
public interface ChatRepository {

    Long save(ChatData chatData);
}
