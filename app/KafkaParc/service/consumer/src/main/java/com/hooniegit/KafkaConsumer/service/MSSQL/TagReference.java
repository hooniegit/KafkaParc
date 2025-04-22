package com.hooniegit.KafkaConsumer.service.MSSQL;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Reference Data Component
 * - Manage Reference Datas
 */
@Component
public class TagReference {

    @Getter
    private ConcurrentHashMap<Integer, Integer> ids = new ConcurrentHashMap<>();

    /**
     * Update Reference Datas
     * @param ids
     */
    public void updateIds(ConcurrentHashMap<Integer, Integer> ids) {
        this.ids = ids;
    }

}
