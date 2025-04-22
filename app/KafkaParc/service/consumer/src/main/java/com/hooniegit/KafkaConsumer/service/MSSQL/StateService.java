package com.hooniegit.KafkaConsumer.service.MSSQL;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * MSSQL Service for Group Unit
 * - [Periodical] Update Reference Datas(List, Map)
 */
@Service
public class StateService {

    private final JdbcTemplate stateJdbcTemplate;
    private final StateReference referenceMap;

    @Autowired
    public StateService(@Qualifier("groupJdbcTemplate") JdbcTemplate jdbcTemplate,
                        StateReference referenceMap) {
        this.stateJdbcTemplate = jdbcTemplate;
        this.referenceMap = referenceMap;
    }

    /**
     * [Init] Run Update Tasks
     */
    @PostConstruct
    public void initialTask() {
        updateIds();
        updateGroupIds();
    }

    /**
     * [Cron] Run Update Tasks Every 3 Minutes
     * @throws Exception
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void periodicalTask() throws Exception{
        updateIds();
        updateGroupIds();
    }

    /**
     * Update Tag Reference List
     */
    private void updateIds() {
        String getSql = """
            SELECT Tool_index, TagID 
            FROM ToolState.dbo.TagList 
            WHERE ToolState = 'State';
        """;

        referenceMap.updateIds(stateJdbcTemplate.query(getSql, rs -> {
            ConcurrentHashMap<Integer, Integer> resultMap = new ConcurrentHashMap<>();
            while (rs.next()) {
                resultMap.put(rs.getInt("Tool_index"), rs.getInt("TagID"));
            }
            return resultMap;
        }));
    }

    /**
     * Update Group Reference List
     */
    private void updateGroupIds() {
        String sql = """
            SELECT Tool_index, 
                   STRING_AGG(CAST(TagID AS VARCHAR), ',') 
                       WITHIN GROUP (ORDER BY 
                           CASE ToolState 
                               WHEN 'StatusOne' THEN 1 
                               WHEN 'StatusTwo' THEN 2 
                               WHEN 'StatusThree' THEN 3 
                           END 
                       ) AS id_list 
            FROM ToolState.dbo.TagList 
            WHERE ToolState != 'State' 
            GROUP BY Tool_index;
        """;

        referenceMap.updateGroups(stateJdbcTemplate.query(sql, rs -> {
            ConcurrentHashMap<Integer, Integer[]> resultMap = new ConcurrentHashMap<>();
            while (rs.next()) {
                int groupId = rs.getInt("Tool_index");
                String[] parts = rs.getString("id_list").split(",");
                Integer[] ids = new Integer[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    ids[i] = Integer.parseInt(parts[i]);
                }
                resultMap.put(groupId, ids);
            }
            return resultMap;
        }));
    }

}
