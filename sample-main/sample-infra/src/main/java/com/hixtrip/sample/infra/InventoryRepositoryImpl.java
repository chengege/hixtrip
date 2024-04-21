package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 库存缓存前置key
     */
    private final String INVENTORY_REPOSITORY_KEY_PRE = "order:inventory:";

    private final String SELLABLE_QUANTITY_FIELD = "sellable";
    private final String WITHHOLDING_QUANTITY_FIELD = "withholding";
    private final String OCCUPIED_QUANTITY_FIELD = "occupied";

    private final String CHANGE_INVENTORY_LUA = "/lua/changeInventory.lua";

    @Override
    public Integer getInventory(String skuId) {
        String key = INVENTORY_REPOSITORY_KEY_PRE + skuId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Integer sellableQuantity = (Integer) entries.getOrDefault(SELLABLE_QUANTITY_FIELD,0);
        Integer withholdingQuantity = (Integer) entries.getOrDefault(WITHHOLDING_QUANTITY_FIELD,0);
        Integer occupiedQuantity = (Integer) entries.getOrDefault(OCCUPIED_QUANTITY_FIELD,0);
        return sellableQuantity - withholdingQuantity - occupiedQuantity;
    }

    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(CHANGE_INVENTORY_LUA)));
        redisScript.setResultType(Boolean.class);

        return redisTemplate.execute(redisScript,Collections.singletonList(skuId),sellableQuantity,withholdingQuantity,occupiedQuantity);
    }

}
