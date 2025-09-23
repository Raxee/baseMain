package com.ruoyi.common.core.redis;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的对话记忆存储实现
 */
@Service
@Slf4j
public class RedisChatMemory implements ChatMemory {
    // Redis 键前缀，避免键冲突
    private static final String KEY_PREFIX = "chat:memory:";
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisChatMemory(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加单条消息到对话历史
     *
     * @param conversationId 对话 ID
     * @param message        消息对象
     */
    @Override
    public void add(@NotNull String conversationId, @NotNull Message message) {
        add(conversationId, List.of(message));
    }

    /**
     * 添加多条消息到对话历史
     *
     * @param conversationId 对话 ID
     * @param messages       消息列表
     */
    @Override
    public void add(@NotNull String conversationId, @NotNull List<Message> messages) {
        // 获取现有消息列表
        List<Message> existingMessages = getFromRedis(conversationId);
        // 合并消息
        existingMessages.addAll(messages);
        // 保存更新后的消息列表
        setToRedis(conversationId, existingMessages);

        // 异步存储到MySQL数据库(若自己实现了可以使用)
//        mySQLChatMemoryStore.storeMessages(conversationId, messages);

        // 检查消息数量，如果超过20条则删除多余部分，只保留最新的20条
        if (existingMessages.size() > 20) {
            trimConversation(conversationId);
        }

        log.debug("已向对话 [{}] 添加 {} 条消息，当前总消息数: {}",
                conversationId, messages.size(), Math.min(existingMessages.size(), 20));
    }

    /**
     * 获取对话消息
     *
     * @param conversationId 对话 ID
     * @return 消息列表
     */
    @NotNull
    @Override
    public List<Message> get(@NotNull String conversationId) {
        List<Message> allMessages = getFromRedis(conversationId);
        return allMessages.stream()
                .toList();
    }

    /**
     * 获取对话的最近 N 条消息
     *
     * @param conversationId 对话 ID
     * @param lastN          最近的消息数量
     * @return 消息列表
     */
    public List<Message> get(String conversationId, int lastN) {
        List<Message> allMessages = getFromRedis(conversationId);
        int skip = Math.max(0, allMessages.size() - lastN);
        return allMessages.stream()
                .skip(skip)
                .toList();
    }

    /**
     * 清空对话历史
     *
     * @param conversationId 对话 ID
     */
    @Override
    public void clear(@NotNull String conversationId) {
        String key = getRedisKey(conversationId);
        redisTemplate.delete(key);
        log.debug("已清空对话 [{}] 的历史消息", conversationId);
    }

    /**
     * 清理对话历史，只保留最新的20条消息
     *
     * @param conversationId 对话 ID
     */
    public void trimConversation(String conversationId) {
        List<Message> allMessages = getFromRedis(conversationId);
        if (allMessages.size() > 20) {
            // 只保留最新的20条消息
            List<Message> recentMessages = allMessages.subList(allMessages.size() - 20, allMessages.size());
            setToRedis(conversationId, recentMessages);
            log.debug("已清理对话 [{}] 的历史消息，从 {} 条减少到 20 条", conversationId, allMessages.size());
        }
    }

    /**
     * 获取所有对话ID
     *
     * @return 对话ID集合
     */
    public List<String> getAllConversationIds() {
        Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
        // 移除前缀，只返回conversationId
        List<String> conversationIds = new ArrayList<>();
        for (String key : keys) {
            conversationIds.add(key.substring(KEY_PREFIX.length()));
        }
        return conversationIds;
    }

    /**
     * 从 Redis 获取消息列表
     */
    private List<Message> getFromRedis(String conversationId) {
        String key = getRedisKey(conversationId);
        Object value = redisTemplate.opsForValue().get(key);
        // 处理空值或类型不匹配的情况
        if (value == null) {
            return new ArrayList<>();
        }
        if (!(value instanceof List)) {
            log.error("对话 [{}] 的消息存储格式不正确，预期为 List，实际为: {}",
                    conversationId, value.getClass().getName());
            return new ArrayList<>();
        }
        List<Message> messages = new ArrayList<>();
        for (Object item : (List<?>) value) {
            if (item instanceof JSONObject jsonItem) {
                messages.add(parseJsonObjectToMessage(jsonItem));
            }
        }
        return messages;
    }

    /**
     * 将JSONObject对象转换为Message类型对象
     * @param jsonItem JSONObject
     * @return Message
     */
    private Message parseJsonObjectToMessage(JSONObject jsonItem) {
        if (!jsonItem.containsKey("messageType")) return null;
        String messageType = jsonItem.get("messageType").toString();
        if ("ASSISTANT".equals(messageType)) {
            // AI说的
            return new AssistantMessage(jsonItem.get("text").toString());
        } else if ("USER".equals(messageType)) {
            // 用户说的
            return new UserMessage(jsonItem.get("text").toString());
        } else if("SYSTEM".equals(messageType)) {
            // 系统提示
            return new SystemMessage(jsonItem.get("text").toString());
        }
        return null;
    }

    /**
     * 将消息列表存入 Redis
     */
    private void setToRedis(String conversationId, List<Message> messages) {
        String key = getRedisKey(conversationId);
        redisTemplate.opsForValue().set(key, messages, 1L, TimeUnit.DAYS);
    }

    /**
     * 生成带前缀的 Redis 键
     */
    private String getRedisKey(String conversationId) {
        return KEY_PREFIX + conversationId;
    }
}



