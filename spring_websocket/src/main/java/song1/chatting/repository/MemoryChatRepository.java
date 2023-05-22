package song1.chatting.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import song1.chatting.domain.ChatData;

import java.util.HashMap;

@Repository
@Slf4j
public class MemoryChatRepository implements ChatRepository {

    private HashMap<Long, ChatData> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public Long save(ChatData chatData) {
        store.put(++sequence, chatData);
        log.info("저장됨 {}", store.get(sequence));
        return sequence;
    }
}
