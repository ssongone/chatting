package song1.chatting.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import song1.chatting.domain.ChatData;

import java.util.HashMap;
import java.util.Map;


@Repository
@Primary
@Slf4j
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(ChatData chatData) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        log.info("db save {}", chatData);
        jdbcInsert.withTableName("chat").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", chatData.getName());
        parameters.put("message", chatData.getMessage());
        parameters.put("time", chatData.getTime());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();
    }
}
