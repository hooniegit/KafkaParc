package com.hooniegit.StatusOneRecorder.service.MSSQL;

import com.hooniegit.SourceData.Interface.TagData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MSSQL Service
 * - [Periodical] Update Reference
 * - Check Value Changes & Write CSV Datas
 */
@Service
public class StateService {

    private final JdbcTemplate stateJdbcTemplate;
    private final StateReference referenceMap;
    private final String bufferPath = "/Users/a1234/Buffer/StatusOne/";

    @Autowired
    public StateService(JdbcTemplate jdbcTemplate,
                        StateReference referenceMap) {
        this.stateJdbcTemplate = jdbcTemplate;
        this.referenceMap = referenceMap;
    }

    /**
     * [Init] Run Update Tasks
     */
    @PostConstruct
    public void initialTask() {
        initialize();
        update();
    }

    /**
     * [Cron] Run Update Task Every 3 Minutes
     * @throws Exception
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void periodicalTask() throws Exception {
        update();
    }

    /**
     * Initialize Reference Map
     */
    private void initialize() {
        String getSql = """
        SELECT TagID, Value
        FROM ToolState.dbo.Archive
        WHERE TagID IN (
            SELECT TagID
            FROM ToolState.dbo.TagList
            WHERE ToolState = 'StatusTwo'
        )
        ORDER BY TagID ASC;
        """;

        referenceMap.intialize(stateJdbcTemplate.query(getSql, rs -> {
            ConcurrentHashMap<Integer, String> resultMap = new ConcurrentHashMap<>();
            while (rs.next()) {
                resultMap.put(rs.getInt("TagID"), rs.getString("Value"));
            }
            return resultMap;
        }));

        System.out.println("Initialization Success : " + referenceMap.getIdMap().size());

    }

    /**
     * Update Reference Map
     */
    private void update() {
        // SELECT TagID & Create List
        String getSql = """
        SELECT TagID 
        FROM ToolState.dbo.TagList 
        WHERE ToolState = 'StatusTwo'
        ORDER BY TagID ASC;
        """;
        List<Integer> tagIdList = stateJdbcTemplate.query(getSql, (rs, rowNum) -> rs.getInt("TagID"));

        System.out.println(">> TagID List Size :: " + tagIdList.size());

        // Update List if Database is Updated
        this.referenceMap.update(tagIdList);
    }

    /**
     * Check Data Changes & Run File Write Task
     * @param dataList
     */
    public void check(List<TagData<String>> dataList) {
        List<TagData<String>> entryList = new ArrayList<>();

        // Check if Database is Updated
        for (TagData<String> data : dataList) {
            if (!this.referenceMap.getIdMap().get(data.getId()).equals(data.getValue())) {
                System.out.println("[BF]" + this.referenceMap.getIdMap().get(data.getId()) + " -> [AF]" + data.getValue());
                entryList.add(data);
            }
        }

        // Insert Data & Update Map if Database is Updated
        if (!entryList.isEmpty()) {
            System.out.println(">> CHANGED DATA :: " + entryList.size());
            this.referenceMap.updateMap(entryList);
            filewrite(entryList);
            // insertMultipleRows(entryList);
        }
    }

    /**
     * Write Datas to New CSV File
     * @param dataList
     */
    protected void filewrite(List<TagData<String>> dataList) {
        if (dataList.isEmpty()) return;

        String filename = dataList.get(0).getTimestamp() + ".csv";
        Path filePath = Path.of(bufferPath, filename);

        // Try With Close :: Write Datas
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            // Header
            writer.write("TagId,Value,Timestamp");
            writer.newLine();

            // Datas
            for (TagData<String> tagData : dataList) {
                String line = String.format("%d,%s,%s",
                        tagData.getId(),
                        tagData.getValue(),
                        tagData.getTimestamp()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write TagData to CSV file: " + filePath, e);
        }
    }

    /**
     * Insert Multiple Rows to MSSQL Database
     * @param dataList
     */
    protected void insertMultipleRows(List<TagData<String>> dataList) {
        String sql = """
        INSERT INTO TagValue (TagID, Timestamp, Value) 
        VALUES (?, ?, ?)
        """;

        stateJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TagData<String> row = dataList.get(i);
                ps.setInt(1, row.getId());
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.parse(row.getTimestamp())));
                ps.setString(3, row.getValue());
            }

            @Override
            public int getBatchSize() {
                return dataList.size();
            }
        });
    }

}
