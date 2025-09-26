package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysPrompt;
import com.ruoyi.system.mapper.SysPromptMapper;
import com.ruoyi.system.service.ISysPromptService;
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
}
