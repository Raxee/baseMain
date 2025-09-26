package com.ruoyi.web.controller.ai;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.ChatRequest;
import com.ruoyi.common.core.domain.entity.SysPrompt;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysPromptService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysPromptService sysPromptService;

    private final ChatClient chatClient;

    @Autowired
    public AiChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 普通聊天，等到内容全部接收后返回给前端
     * @param chatRequest
     * @return
     */
    @GetMapping("/chat")
    public String chat(ChatRequest chatRequest) {
        return chatClient.prompt(getPrompt(chatRequest.getChatType()))
                .user(chatRequest.getUserMsg())
                .call()
                .content();
    }




    /**
     * 根据chatType获取对应的promptContent
     * @param chatType 聊天类型
     * @return prompt内容
     */
    private String getPrompt(String chatType) {
        if (StringUtils.isEmpty(chatType)) return  "";

        // 从Redis中获取prompt
        String cacheKey = CacheConstants.PREHEAT_PROMPT_KEY + chatType;
        String promptContent = redisCache.getCacheObject(cacheKey);

        if (StringUtils.isEmpty(promptContent)) {
            // 如果Redis中没有获取到，则从数据库获取并放到Redis里
            SysPrompt sysPrompt = sysPromptService.selectPromptByChatType(chatType);
            if (sysPrompt != null) {
                promptContent = sysPrompt.getPromptContent();
                // 放到Redis中
                redisCache.setCacheObject(cacheKey, promptContent);
            }
        }

        return promptContent;
    }

}
