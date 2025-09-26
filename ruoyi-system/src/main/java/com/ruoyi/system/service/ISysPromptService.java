package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysPrompt;

import java.util.List;

public interface ISysPromptService {
    List<SysPrompt> selectPromptList(SysPrompt sysPrompt);
}
