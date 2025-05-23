package com.hooniegit.KafkaConsumer.service.MSSQL;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * MSSQL Service for Tag Unit
 * - [Periodical] Update Reference Datas
 */
@Service
public class TagService {

    private final JdbcTemplate stateJdbcTemplate;
    private final TagReference referenceMap;

    @Autowired
    public TagService(@Qualifier("tagJdbcTemplate") JdbcTemplate jdbcTemplate,
                      TagReference referenceMap) {
        this.stateJdbcTemplate = jdbcTemplate;
        this.referenceMap = referenceMap;
    }

    /**
     * [Init] Run Update Task
     */
    @PostConstruct
    public void initialTask() {
        updateIds();
    }

    /**
     * [Cron] Run Update Task for Every 3 Minutes
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void periodicalTask() {
        try {
            updateIds();
        } catch (Exception ex) {
            ex.printStackTrace(); // 로깅 권장
        }
    }

    /**
     * Update Reference Datas
     */
    private void updateIds() {
        String getSql = """
            SELECT paramid, id 
            FROM ctc_kafka.dbo.kf_tag;
        """;

        referenceMap.updateIds(stateJdbcTemplate.query(getSql, rs -> {
            ConcurrentHashMap<Integer, Integer> resultMap = new ConcurrentHashMap<>();
            while (rs.next()) {
                resultMap.put(rs.getInt("paramid"), rs.getInt("id"));
            }
            return resultMap;
        }));
    }

}

