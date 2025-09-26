package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysPrompt;

import java.util.List;

public interface ISysPromptService {
    List<SysPrompt> selectPromptList(SysPrompt sysPrompt);

    SysPrompt selectPromptById(Long promptId);

    int insertPrompt(SysPrompt sysPrompt);

    int updatePrompt(SysPrompt sysPrompt);

    int deletePromptByIds(Long[] promptIds);

    SysPrompt selectPromptByChatType(String chatType);
}
