package com.ruoyi.system.preheat;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysPrompt;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.system.service.ISysPromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统预热任务加载到Redis中
 * 应用启动时将prompt表数据加载到Redis中
 */
@Slf4j
@Component
public class RedisPreheatTask  implements CommandLineRunner {

    @Autowired
    private ISysPromptService sysPromptService;

    @Autowired
    private RedisCache redisCache;

    /**
     * Redis数据预热
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("开始预热系统数据...");
        preheatPrompt();
    }

    /**
     * 预热prompt数据
     */
    private void preheatPrompt() {
        log.info("开始预热prompt数据...");

        // 查询所有prompt数据
        List<SysPrompt> promptList = sysPromptService.selectPromptList(new SysPrompt());

        // 将数据加载到Redis中
        for (SysPrompt prompt : promptList) {
            String cacheKey = CacheConstants.PREHEAT_PROMPT_KEY + prompt.getChatType();
            redisCache.setCacheObject(cacheKey, prompt.getPromptContent());
        }

        log.info("预热prompt数据完成，共{}条数据", promptList.size());
    }

}
