package com.hooniegit.Inserter.service.MSSQL;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * MSSQL Service
 * - [Periodical] Update Archive Table
 */
@Service
public class StateService {

    private final JdbcTemplate stateJdbcTemplate;

    @Autowired
    public StateService(@Qualifier("groupJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.stateJdbcTemplate = jdbcTemplate;
    }

    /**
     * Initialization :: Run Update Task
     */
    @PostConstruct
    public void initialTask() {
        update("Init");
    }

    /**
     * Scheduled :: Run Update Task for Every 3 Minutes
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void periodicalTask() {
        update("Cron");
    }

    /**
     * Task Method :: Update Archive Table
     */
    protected void update(String type) {
        String sql = """
            MERGE INTO Archive AS target
            USING (
                SELECT
                    TagID,
                    Value
                FROM TagValue v
                WHERE v.ID IN (
                    SELECT MAX(ID)
                    FROM TagValue
                    GROUP BY TagID
                )
            ) AS source
            ON target.TagID = source.TagID
            WHEN MATCHED THEN
                UPDATE SET target.Value = source.Value
            WHEN NOT MATCHED THEN
                INSERT (TagID, Value) VALUES (source.TagID, source.Value);
        """;

        stateJdbcTemplate.update(sql);
        System.out.println("[" + type + "] Database Updated Successfully");
    }

}

