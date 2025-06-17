package com.activity.util;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public Long nextId(Object entity) {
        return idWorker.nextId();
    }
}
