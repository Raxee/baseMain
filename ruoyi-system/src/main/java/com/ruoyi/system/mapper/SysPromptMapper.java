package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysPrompt;

import java.util.List;

public interface SysPromptMapper {

    /**
     * 查询SysPrompt
     *
     * @param SysPrompt SysPrompt对象
     * @return SysPrompt
     */
    SysPrompt selectPrompt(SysPrompt SysPrompt);

    /**
     * 查询SysPrompt列表
     *
     * @param SysPrompt SysPrompt对象
     * @return SysPrompt集合
     */
    List<SysPrompt> selectPromptList(SysPrompt SysPrompt);

    /**
     * 根据ID查询SysPrompt
     *
     * @param SysPromptId SysPromptID
     * @return SysPrompt
     */
    SysPrompt selectPromptById(Long SysPromptId);

    /**
     * 检查chatType是否唯一
     *
     * @param chatType 聊天类型
     * @return SysPrompt
     */
    SysPrompt checkChatTypeUnique(String chatType);

    /**
     * 新增SysPrompt
     *
     * @param SysPrompt SysPrompt对象
     * @return 结果
     */
    int insertPrompt(SysPrompt SysPrompt);

    /**
     * 修改SysPrompt
     *
     * @param SysPrompt SysPrompt对象
     * @return 结果
     */
    int updatePrompt(SysPrompt SysPrompt);

    /**
     * 删除SysPrompt
     *
     * @param SysPromptId SysPromptID
     * @return 结果
     */
    int deletePromptById(Long SysPromptId);

    /**
     * 批量删除SysPrompt
     *
     * @param SysPromptIds 需要删除的数据ID
     * @return 结果
     */
    int deletePromptByIds(Long[] SysPromptIds);
}
