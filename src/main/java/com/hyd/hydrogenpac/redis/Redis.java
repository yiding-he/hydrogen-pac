package com.hyd.hydrogenpac.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.*;

/**
 * RedisTemplate is a totally shit.
 *
 * @author Yiding
 */
@SuppressWarnings({"Anonymous2MethodRef", "unused", "WeakerAccess"})
public class Redis {

    private static final Logger LOG = LoggerFactory.getLogger(Redis.class);

    public static final int DEFAULT_TIMEOUT_SECS = 10;

    private JedisPool pool;

    private ObjectSerializer objectSerializer =new JsonObjectSerializer();

    public Redis(String host, int port, String password, int maxTotal, int timeoutSecs) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        this.pool = new JedisPool(poolConfig, host, port, timeoutSecs * 1000, password);
    }

    public Redis(String host, int port, int maxTotal, int timeoutSecs) {
        this(host, port, null, maxTotal, timeoutSecs);
    }

    public Redis(String host, int port, int maxTotal) {
        this(host, port, maxTotal, DEFAULT_TIMEOUT_SECS);
    }

    // to replace default object serializer
    public void setObjectSerializer(ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    private static void run(JedisPool jedisPool, JedisTask jedisTask) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedisTask.run(jedis);
        }
    }

    private static <T> T run(JedisPool jedisPool, JedisResultTask<T> jedisResultTask) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedisResultTask.run(jedis);
        }
    }

    /**
     * 获取一个值
     *
     * @param key 键
     *
     * @return 值
     */
    public String get(final String key) {
        return run(pool, jedis -> {
            return jedis.get(key);
        });
    }

    /////////////////////////////////////////////////////////

    /**
     * 设置一个值
     *
     * @param key   键
     * @param value 值
     */
    public void set(final String key, final String value) {
        run(pool, jedis -> {
            jedis.set(key, value);
        });
    }

    /////////////////////////////////////////////////////////

    /**
     * 设置一个值并指定超时秒数
     *
     * @param key   键
     * @param ex    超时秒数
     * @param value 值
     */
    public void set(final String key, final int ex, final String value) {
        run(pool, jedis -> {
            jedis.setex(key, ex, value);
        });
    }

    /**
     * 仅当键不存在时设置一个值
     *
     * @param key   键
     * @param value 值
     *
     * @return 0 表示键已存在
     */
    public int setOnlyIfNotExists(final String key, final String value) {
        return run(pool, jedis -> {
            return jedis.setnx(key, value).intValue();
        });
    }

    /**
     * 删除一个或多个键
     *
     * @param keys 键
     *
     * @return 被删除的键的数量
     */
    public int delete(final String... keys) {
        return run(pool, jedis -> {
            return jedis.del(keys).intValue();
        });
    }

    /**
     * 删除缓存中的所有键
     *
     * @return 结果状态码
     */
    public String deleteAll() {
        return run(pool, BinaryJedis::flushAll);
    }

    public void scan(final String pattern, final KeyHandler handler) {
        run(pool, jedis -> {
            ScanParams params = new ScanParams();
            params.match(pattern);
            ScanResult<String> scanResult;
            String stringCursor = "0";

            do {
                scanResult = jedis.scan(stringCursor, params);
                stringCursor = scanResult.getStringCursor();
                for (String key : scanResult.getResult()) {
                    handler.handle(Redis.this, key);
                }
            } while (!("0".equals(stringCursor)));
        });
    }

    public void scan(final String pattern, final int maxReturn, final KeyHandler handler) {
        run(pool, jedis -> {
            ScanParams params = new ScanParams();
            params.match(pattern);
            ScanResult<String> scanResult;
            String stringCursor = "0";
            int counter = 0;

            do {
                scanResult = jedis.scan(stringCursor, params);
                stringCursor = scanResult.getStringCursor();

                for (String key : scanResult.getResult()) {
                    counter++;
                    if (counter > maxReturn) {
                        return;
                    }

                    handler.handle(Redis.this, key);
                }
            } while (!("0".equals(stringCursor)));
        });
    }

    public void deletePattern(String pattern) {
        scan(pattern, Redis::delete);
    }

    /**
     * 查询缓存中总共有多少个键
     *
     * @return 键的数量
     */
    public long dbsize() {
        return run(pool, BinaryJedis::dbSize);
    }

    /**
     * 列出所有符合模板的 key
     *
     * @param pattern 模板
     *
     * @return 所有符合模板的 key
     */
    public Set<String> keys(final String pattern) {
        return run(pool, jedis -> {
            return jedis.keys(pattern);
        });
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 要判断的 key
     *
     * @return 如果存在则返回 true
     */
    public boolean exists(final String key) {
        return run(pool, jedis -> {
            return jedis.exists(key);
        });
    }

    /**
     * 设置相对超时时间
     *
     * @param key     超时的 key
     * @param seconds 从现在开始多少秒后超时
     *
     * @return 0 表示 key 不存在，1 表示设置超时成功
     */
    public int expire(final String key, final int seconds) {
        return run(pool, jedis -> {
            return jedis.expire(key, seconds).intValue();
        });
    }

    /**
     * 设置相对超时时间
     *
     * @param key              超时的 key
     * @param timestampSeconds 超时绝对时间秒数
     *
     * @return 0 表示 key 不存在，1 表示设置超时成功
     */
    public int expireAt(final String key, final long timestampSeconds) {
        return run(pool, jedis -> {
            return jedis.expireAt(key, timestampSeconds).intValue();
        });
    }

    /**
     * 获取一个 List 对象
     *
     * @param key 键
     *
     * @return List 对象
     */
    public RedisList getList(String key) {
        return new RedisList(key, pool, objectSerializer);
    }

    /**
     * 获取一个 Hash 对象
     *
     * @param key 键
     *
     * @return Hash 对象
     */
    public RedisHash getHash(String key) {
        return new RedisHash(this.pool, key);
    }

    /**
     * 获取一个 Set 对象
     *
     * @param key 键
     *
     * @return Set 对象
     */
    public RedisSet getSet(String key) {
        return new RedisSet(pool, key);
    }

    /**
     * 获取一个 Queue 对象，可用作阻塞式的消息队列
     *
     * @param key 键
     *
     * @return Queue 对象
     */
    public RedisQueue getQueue(String key) {
        return new RedisQueue(key, pool, objectSerializer);
    }

    public RedisSortedSet getSortedSet(String key) {
        return new RedisSortedSet(pool, key);
    }

    public RedisCounter getCounter(String key, long initValue) {
        return new RedisCounter(pool, key, initValue);
    }

    public RedisCounter getCounter(String key) {
        return getCounter(key, 0);
    }

    public void watch(String... watchKeys) {
        run(pool, jedis -> {
            jedis.watch(watchKeys);
        });
    }

    public Pipeline pipeline() {
        return run(pool, (JedisResultTask<Pipeline>) BinaryJedis::pipelined);
    }

    public enum Direction {
        Left, Right
    }

    ////////////////////////////////////////////////////////////////

    public interface KeyHandler {

        void handle(Redis redis, String key);
    }

    private interface JedisTask {

        void run(Jedis jedis);
    }

    private interface JedisResultTask<T> {

        T run(Jedis jedis);
    }

    public interface TransactionTask {

        void run(Pipeline transaction);
    }

    /////////////////////////////////////////////////////////

    public static class RedisCounter {

        private JedisPool pool;

        private String key;

        public RedisCounter(JedisPool pool, String key) {
            this.pool = pool;
            this.key = key;
        }

        public RedisCounter(JedisPool pool, final String key, final long initValue) {
            this.pool = pool;
            this.key = key;

            set(initValue);
        }

        public long get() {
            return run(pool, jedis -> {
                return Long.valueOf(jedis.get(key));
            });
        }

        public void set(final long initValue) {
            run(pool, jedis -> {
                jedis.set(key, String.valueOf(initValue));
            });
        }

        public long add(final long value) {
            return run(pool, jedis -> {
                return jedis.incrBy(key, value);
            });
        }

        public long incr() {
            return add(1);
        }
    }

    /////////////////////////////////////////////////////////

    public static class RedisSortedSet {

        private JedisPool pool;

        private String key;

        public RedisSortedSet(JedisPool pool, String key) {
            this.pool = pool;
            this.key = key;
        }

        public int add(final String member, final double score) {
            return run(pool, jedis -> {
                return jedis.zadd(key, score, member).intValue();
            });
        }

        public int add(final Map<String, Double> values) {
            return run(pool, jedis -> {
                return jedis.zadd(key, values).intValue();
            });
        }

        public int countBetween(final String min, final String max) {
            return run(pool, jedis -> {
                return jedis.zcount(key, min, max).intValue();
            });
        }

        public int countBetween(final double min, final double max) {
            return run(pool, jedis -> {
                return jedis.zcount(key, min, max).intValue();
            });
        }

        public double increBy(final String member, final double increment) {
            return run(pool, jedis -> {
                return jedis.zincrby(key, increment, member);
            });
        }

        /**
         * 查询成员的值
         *
         * @param member 成员名字
         *
         * @return 成员的值，如果成员不存在则返回 null
         */
        public Double getScore(final String member) {
            return run(pool, jedis -> {
                return jedis.zscore(key, member);
            });
        }

        /**
         * 查询成员的位置
         *
         * @param member 成员的名字
         *
         * @return 成员的位置，如果不存在则返回 -1
         */
        public int indexOf(final String member) {
            return run(pool, jedis -> {
                Long position = jedis.zrank(key, member);
                return position == null ? -1 : position.intValue();
            });
        }

        public boolean exists() {
            return run(pool, jedis -> {
                return jedis.exists(key);
            });
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * Redis 列表
     */
    public static class RedisList {

        protected final String key;

        protected final JedisPool pool;

        protected final ObjectSerializer objectSerializer;

        public RedisList(String key, JedisPool pool, ObjectSerializer objectSerializer) {
            this.key = key;
            this.pool = pool;
            this.objectSerializer = objectSerializer;
        }

        /**
         * 获取列表长度
         *
         * @return 列表长度
         */
        public long size() {
            return run(pool, jedis -> {
                return jedis.llen(key);
            });
        }

        /**
         * 获取指定位置的元素
         *
         * @param index 位置，负数表示从右边算起
         *
         * @return 元素，index 超出范围则返回 null
         */
        public String get(final int index) {
            return run(pool, jedis -> {
                return jedis.lindex(RedisList.this.key, index);
            });
        }

        /**
         * 在指定位置插入元素
         *
         * @param index 位置，负数表示从右边算起
         * @param value 元素
         */
        public void insert(final int index, final String value) {
            run(pool, jedis -> {
                jedis.lset(key, index, value);
            });
        }

        /**
         * 在列表右边添加元素
         *
         * @param onlyIfListExists 是否仅当列表存在时这么做，若为 false，则自动创建列表
         * @param values           要添加的元素
         */
        public void append(final boolean onlyIfListExists, final String... values) {
            run(pool, jedis -> {
                if (onlyIfListExists) {
                    jedis.rpushx(key, values);
                } else {
                    jedis.rpush(key, values);
                }
            });
        }

        /**
         * 在列表左边添加元素
         *
         * @param onlyIfListExists 是否仅当列表存在时这么做，若为 false，则自动创建列表
         * @param values           要添加的元素。注意添加顺序是按照参数顺序来，所以第一个参数第一个加入，当完成后，最后一个参数在最左边。
         */
        public void prepend(final boolean onlyIfListExists, final String... values) {
            run(pool, jedis -> {
                if (onlyIfListExists) {
                    jedis.lpushx(key, values);
                } else {
                    jedis.lpush(key, values);
                }
            });
        }

        /**
         * 在指定元素的左边添加元素
         *
         * @param pivot 目标元素的值
         * @param value 要插入的元素
         */
        public void insertBefore(final String pivot, final String value) {
            run(pool, jedis -> {
                jedis.linsert(key, BinaryClient.LIST_POSITION.BEFORE, pivot, value);
            });
        }

        /**
         * 在指定元素的右边添加元素
         *
         * @param pivot 目标元素的值
         * @param value 要插入的元素
         */
        public void insertAfter(final String pivot, final String value) {
            run(pool, jedis -> {
                jedis.linsert(key, BinaryClient.LIST_POSITION.AFTER, pivot, value);
            });
        }

        /**
         * 从列表中删除 count 个 value 值
         *
         * @param value 要删除的元素
         * @param count 如果存在多个相同的元素，则指定删除多少个。0表示删除所有，1表示从左边起删除1个，-1表示从右边起删除1个，依此类推。
         *
         * @return 实际被删除的元素个数
         */
        public int remove(final String value, final int count) {
            return run(pool, jedis -> {
                return jedis.lrem(key, count, value).intValue();
            });
        }

        /**
         * 删除列表中除指定范围外的其他所有元素
         *
         * @param startIncluded 指定范围开始位置（含）
         * @param endIncluded   指定范围结束位置（含）
         */
        public void trim(final int startIncluded, final int endIncluded) {
            run(pool, jedis -> {
                jedis.ltrim(key, startIncluded, endIncluded);
            });
        }

        /**
         * 返回子列表。sublist(0, -1) 表示返回所有的值
         *
         * @param startIncluded 开始位置（含）
         * @param endIncluded   结束位置（含）
         *
         * @return 子列表
         */
        public List<String> sublist(final int startIncluded, final int endIncluded) {
            return run(pool, jedis -> {
                return jedis.lrange(key, startIncluded, endIncluded);
            });
        }

        /**
         * 列出所有的值
         *
         * @return 所有的值
         */
        public List<String> all() {
            return sublist(0, -1);
        }

        /**
         * 列出所有的值，并转换成对象
         *
         * @param type 对象类型
         *
         * @return 转换后的对象列表
         */
        public <T> List<T> all(Class<T> type) {
            List<T> list = new ArrayList<>();
            List<String> serializedStringList = all();
            for (String serializedString : serializedStringList) {
                list.add(objectSerializer.deserialize(serializedString, type));
            }
            return list;
        }

        public boolean exists() {
            return run(pool, jedis -> {
                return jedis.exists(key);
            });
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * Redis 哈希
     */
    public static class RedisHash {

        private JedisPool pool;

        private String key;

        public RedisHash(JedisPool pool, String key) {
            this.pool = pool;
            this.key = key;
        }

        /**
         * 获取指定属性值
         *
         * @param field 属性名
         *
         * @return 属性值
         */
        public String get(final String field) {
            return run(pool, jedis -> {
                return jedis.hget(key, field);
            });
        }

        /**
         * 获取指定属性值
         *
         * @param fields 属性名
         *
         * @return 属性值
         */
        public List<String> get(final String... fields) {
            return run(pool, jedis -> {
                return jedis.hmget(key, fields);
            });
        }

        /**
         * 设置属性
         *
         * @param field 属性名
         * @param value 属性值
         */
        public void put(final String field, final String value) {
            run(pool, jedis -> {
                jedis.hset(key, field, value);
            });
        }

        public long incr(final String field) {
            return incr(field, 1);
        }

        public long incr(final String field, final long value) {
            return run(pool, jedis -> {
                return jedis.hincrBy(key, field, value);
            });
        }

        public double incr(final String field, final double value) {
            return run(pool, jedis -> {
                return jedis.hincrByFloat(key, field, value);
            });
        }

        /**
         * 仅当属性名不存在是设置属性
         *
         * @param field 属性名
         * @param value 属性值
         *
         * @return 0表示属性已存在
         */
        public int setOnlyIfNotExists(final String field, final String value) {
            return run(pool, jedis -> {
                return jedis.hsetnx(key, field, value).intValue();
            });
        }

        /**
         * 保存多个属性名和属性值
         *
         * @param map 属性
         */
        public void putAll(final Map<String, String> map) {
            run(pool, jedis -> {
                jedis.hmset(key, map);
            });
        }

        /**
         * 删除一个或多个属性
         *
         * @param fields 属性名
         *
         * @return 被删除的属性数量
         */
        public int delete(final String... fields) {
            return run(pool, jedis -> {
                return jedis.hdel(key, fields).intValue();
            });
        }

        /**
         * 列出所有属性
         *
         * @return 所有属性名
         */
        public List<String> keys() {
            JedisResultTask<List<String>> task = jedis -> new ArrayList<>(jedis.hkeys(key));
            return run(pool, task);
        }

        /**
         * 读取所有属性值并转换为 Map 对象
         *
         * @return 所有属性值
         */
        public Map<String, String> toMap() {
            return run(pool, jedis -> {
                return jedis.hgetAll(key);
            });
        }

        public boolean fieldExists(String field) {
            return run(pool, jedis -> {
                return jedis.hexists(key, field);
            });
        }

        public boolean exists() {
            return run(pool, jedis -> {
                return jedis.exists(key);
            });
        }

        public int size() {
            return run(pool, jedis -> {
                return jedis.hlen(key).intValue();
            });
        }
    }

    /////////////////////////////////////////////////////////

    public static class RedisSet {

        protected JedisPool pool;

        protected String key;

        public RedisSet(JedisPool pool, String key) {
            this.pool = pool;
            this.key = key;
        }

        /**
         * 添加元素
         *
         * @param values 要添加的元素
         *
         * @return 实际被添加的元素数量
         */
        public int add(final String... values) {
            return run(pool, jedis -> {
                return jedis.sadd(key, values).intValue();
            });
        }

        /**
         * 获取集合大小
         *
         * @return 集合大小
         */
        public int size() {
            return run(pool, jedis -> {
                return jedis.scard(key).intValue();
            });
        }

        /**
         * 获取集合中的所有值
         *
         * @return 集合中的所有值
         */
        public List<String> values() {
            JedisResultTask<List<String>> task =
                    jedis -> new ArrayList<>(jedis.smembers(key));
            return run(pool, task);
        }

        /**
         * 判断集合中是否包含指定元素
         *
         * @param value 查询的元素
         *
         * @return 是否包含
         */
        public boolean contains(final String value) {
            return run(pool, jedis -> {
                return jedis.sismember(key, value);
            });
        }

        public boolean exists() {
            return run(pool, jedis -> {
                return jedis.exists(key);
            });
        }
    }

    /////////////////////////////////////////////////////////

    /**
     * Redis 队列/消息队列
     */
    public static class RedisQueue extends RedisList {

        public RedisQueue(String key, JedisPool pool, ObjectSerializer objectSerializer) {
            super(key, pool, objectSerializer);
        }

        public List<String> popAll(Direction direction) {
            List<String> all = all();
            clear();

            if (direction == Direction.Right) {
                Collections.reverse(all);
            }
            return all;
        }

        public <T> List<T> popAll(Direction direction, Class<T> type) {
            List<String> all = popAll(direction);
            List<T> result = new ArrayList<>();
            for (String serializedString : all) {
                result.add(objectSerializer.deserialize(serializedString, type));
            }
            return result;
        }

        /**
         * 加入一个或多个值到队列
         *
         * @param direction 方向
         * @param values    要加入的值
         *
         * @return 队列的新长度
         */
        public long push(final Direction direction, final String... values) {
            return run(pool, jedis -> {
                if (direction == Direction.Left) {
                    return jedis.lpush(key, values);
                } else {
                    return jedis.rpush(key, values);
                }
            });
        }

        /**
         * 加入一个或多个对象到队列
         *
         * @param direction 方向
         * @param objects   要加入的对象
         *
         * @return 队列的新长度
         */
        public long push(final Direction direction, final Object... objects) {
            String[] jsons = new String[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                jsons[i] = objectSerializer.serialize(object);
            }

            return push(direction, jsons);
        }

        /**
         * 读取并删除一个值
         *
         * @param direction 方向
         *
         * @return null 或队列中的值
         */
        public String pop(final Direction direction) {
            return run(pool, jedis -> {
                if (direction == Direction.Left) {
                    return jedis.lpop(key);
                } else {
                    return jedis.rpop(key);
                }
            });
        }

        /**
         * 读取并删除一个值，返回该值转换成的对象
         *
         * @param direction 方向
         * @param type      对象类型
         *
         * @return null 或队列中的值
         */
        public <T> T popObject(final Direction direction, Class<T> type) {
            String serializedString = pop(direction);
            return serializedString == null ? null :
                    objectSerializer.deserialize(serializedString, type);
        }

        /**
         * 阻塞式 pop
         *
         * @param direction 读取方向
         * @param timeout   超时秒数，0表示无限制
         *
         * @return null 或队列中的值
         */
        public String popBlocking(final Direction direction, final int timeout) {
            return run(pool, jedis -> {
                List<String> list;

                if (direction == Direction.Left) {
                    list = jedis.blpop(timeout, key);
                } else {
                    list = jedis.brpop(timeout, key);
                }

                if (list == null || list.isEmpty()) {
                    return null;
                } else {
                    return list.get(1);
                }
            });
        }

        public <T> T popBlocking(Direction direction, int timeout, Class<T> type) {
            String serializedString = popBlocking(direction, timeout);
            return serializedString == null ? null :
                    objectSerializer.deserialize(serializedString, type);
        }

        public void clear() {
            run(this.pool, jedis -> {
                jedis.del(RedisQueue.this.key);
            });
        }
    }
}
