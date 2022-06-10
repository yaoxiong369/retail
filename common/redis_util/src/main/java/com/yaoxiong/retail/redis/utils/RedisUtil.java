package com.yaoxiong.retail.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     */
    public RedisConnectionFactory getConnectionFactory() {
        return this.redisTemplate.getConnectionFactory();
    }

    /**
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**

     *
     * @param node redis
     */
    public void flushDB(RedisClusterNode node) {
        this.redisTemplate.opsForCluster().flushDb(node);
    }

    /**

     *
     * @param key
     * @param value
     * @param time
     */
    public void setExpire(final byte[] key, final byte[] value, final long time) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.setEx(key, time, value);
            return 1L;
        });
    }

    /**

     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }
    public void setExpire(final String key, final Object value, final long time) {
        this.setExpire(key, value, time, TimeUnit.SECONDS);
    }
    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        byte[] rawValue = rawValue(value, valueSerializer);

        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                potentiallyUsePsetEx(connection);
                return null;
            }
            public void potentiallyUsePsetEx(RedisConnection connection) {
                if (!TimeUnit.MILLISECONDS.equals(timeUnit) || !failsafeInvokePsetEx(connection)) {
                    connection.setEx(rawKey, TimeoutUtils.toSeconds(time, timeUnit), rawValue);
                }
            }
            private boolean failsafeInvokePsetEx(RedisConnection connection) {
                boolean failed = false;
                try {
                    connection.pSetEx(rawKey, time, rawValue);
                } catch (UnsupportedOperationException e) {
                    failed = true;
                }
                return !failed;
            }
        }, true);
    }

    /**

     *
     * @param keys
     * @param values
     * @param time
     */
    public void setExpire(final String[] keys, final Object[] values, final long time) {
        for (int i = 0; i < keys.length; i++) {
            redisTemplate.opsForValue().set(keys[i], values[i], time, TimeUnit.SECONDS);
        }
    }


    /**

     *
     * @param keys   the keys
     * @param values the values
     */
    public void set(final String[] keys, final Object[] values) {
        for (int i = 0; i < keys.length; i++) {
            redisTemplate.opsForValue().set(keys[i], values[i]);
        }
    }


    /**

     *
     * @param key   the key
     * @param value the value
     */
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**

     *
     * @param keyPatten the key patten
     * @return the set
     */
    public Set<String> keys(final String keyPatten) {
        return redisTemplate.keys(keyPatten + "*");
    }

    /**

     *
     * @param key the key
     * @return the byte [ ]
     */
    public byte[] get(final byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    }

    /**

     *
     * @param key the key
     * @return the string
     */
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }
    /**

     *
     * @param key the key
     * @param valueSerializer
     * @return the string
     */
    public Object get(final String key, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        return redisTemplate.execute(connection -> deserializeValue(connection.get(rawKey), valueSerializer), true);
    }

    /**
     * Ops for hash hash operations.
     *
     * @return the hash operations
     */
    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**

     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public void putHashValue(String key, String hashKey, Object hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    /**

     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public Object getHashValues(String key, String hashKey) {
        return opsForHash().get(key, hashKey);
    }

    /**

     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    public void delHashValues(String key, Object... hashKeys) {
        opsForHash().delete(key, hashKeys);
    }

    /**

     *
     * @param key the key
     * @return the hash value
     */
    public Map<String, Object> getHashValue(String key) {
        return opsForHash().entries(key);
    }

    /**

     *
     * @param key the key
     * @param map the map
     */
    public void putHashValues(String key, Map<String, Object> map) {
        opsForHash().putAll(key, map);
    }

    /**

     *
     * @return the long
     */
    public long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**

     *
     * @return the string
     */
    public String flushDB() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    /**

     *
     * @param key the key
     * @return the boolean
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    /**

     *
     * @param keys the keys
     * @return the long
     */
    public boolean del(final String... keys) {
        boolean result = false;
        for (String key : keys) {
            result = redisTemplate.delete(key);
        }
        return result;
    }

    /**

     *
     * @param key the key
     * @return the long
     */
    public long incr(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**

     *
     * @return the list operations
     */
    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    /**

     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long leftPush(String key, Object value) {
        return opsForList().leftPush(key, value);
    }

    /**

     * @param key the key
     * @return the string
     */
    public Object leftPop(String key) {
        return opsForList().leftPop(key);
    }

    /**

     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long in(String key, Object value) {
        return opsForList().rightPush(key, value);
    }

    /**

     * @param key the key
     * @return the string
     */
    public Object rightPop(String key) {
        return opsForList().rightPop(key);
    }


    /**

     * @param key the key
     * @return the long
     */
    public Long length(String key) {
        return opsForList().size(key);
    }


    /**

     * @param key   the key
     * @param i     the
     * @param value the value
     */
    public void remove(String key, long i, Object value) {
        opsForList().remove(key, i, value);
    }

    /**

     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void set(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    /**

     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public List<Object> getList(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    /**
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @param valueSerializer
     * @return the list
     */
    public List<Object> getList(String key, int start, int end, RedisSerializer<Object> valueSerializer) {
        byte[] rawKey = rawKey(key);
        return redisTemplate.execute(connection -> deserializeValues(connection.lRange(rawKey, start, end), valueSerializer), true);
    }

    /**
     * @param key  the key
     * @param list the list
     * @return the long
     */
    public Long leftPushAll(String key, List<String> list) {
        return opsForList().leftPushAll(key, list);
    }

    /**
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void insert(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");

        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        RedisSerializer<Object> redisSerializer = (RedisSerializer<Object>)redisTemplate.getKeySerializer();
        return redisSerializer.serialize(key);
    }
    private byte[] rawValue(Object value, RedisSerializer valueSerializer) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        return valueSerializer.serialize(value);
    }

    private List deserializeValues(List<byte[]> rawValues, RedisSerializer<Object> valueSerializer) {
        if (valueSerializer == null) {
            return rawValues;
        }
        return SerializationUtils.deserialize(rawValues, valueSerializer);
    }

    private Object deserializeValue(byte[] value, RedisSerializer<Object> valueSerializer) {
        if (valueSerializer == null) {
            return value;
        }
        return valueSerializer.deserialize(value);
    }
}
