local key = "order:inventory:" .. KEYS[1]

--- local inventory = redis.call("hget", key)


--- 找不到key就返回0，前面业务流程已判断，通常情况需要判断此处可以省略
--- if inventory == false then
---    return 0
--- end

--- 获取变化值
local sellableValue = tonumber(ARGV[1])
local withholdingValue = tonumber(ARGV[2])
local occupiedValue = tonumber(ARGV[3])

--- 获取库存
local currentSellableValue = tonumber(redis.call('hget', key, sellableKey)) or 0
local currentWithholdingValue = tonumber(redis.call('hget', key, withholdingKey)) or 0
local currentOccupiedValue = tonumber(redis.call('hget', key, occupiedKey)) or 0


local sellable = currentSellableValue + currentSellableValue
local withholding = currentWithholdingValue + withholdingValue
local occupied = currentOccupiedValue + occupiedValue

--- 不符合规则返回false
if sellable - withholding - occupied < 0 then
    return false
end

--- 客气气事务
redis.call('MULTI')

redis.call("hincrby", key, "sellable", sellableValue)
redis.call("hincrby", key, "withholding", withholdingValue)
redis.call("hincrby", key, "occupied", occupiedValue)

redis.call('EXEC')

--- 执行成功返回1
return true
