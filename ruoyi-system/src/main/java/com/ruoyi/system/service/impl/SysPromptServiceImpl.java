package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysPrompt;
import com.ruoyi.system.mapper.SysPromptMapper;
import com.ruoyi.system.service.ISysPromptService;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPromptServiceImpl implements ISysPromptService {

    @Autowired
    private SysPromptMapper sysPromptMapper;

    @Override
    public List<SysPrompt> selectPromptList(SysPrompt sysPrompt) {
        return sysPromptMapper.selectPromptList(sysPrompt);
    }

    @Override
    public SysPrompt selectPromptById(Long promptId) {
        return sysPromptMapper.selectPromptById(promptId);
    }

    @Override
    public int insertPrompt(SysPrompt sysPrompt) {
        return sysPromptMapper.insertPrompt(sysPrompt);
    }

    @Override
    public int updatePrompt(SysPrompt sysPrompt) {
        return sysPromptMapper.updatePrompt(sysPrompt);
    }

    @Override
    public int deletePromptByIds(Long[] promptIds) {
        return sysPromptMapper.deletePromptByIds(promptIds);
    }

    @Override
    public SysPrompt selectPromptByChatType(String chatType) {
        SysPrompt sysPrompt = new SysPrompt();
        sysPrompt.setChatType(chatType);
        List<SysPrompt> sysPrompts = sysPromptMapper.selectPromptList(sysPrompt);
        // 按说只应该查出一个，但是为了防止数据异常，这里取第一个
        return !sysPrompts.isEmpty() ? sysPrompts.get(0) : null;
    }
}
