package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysPrompt extends BaseEntity {

    private Long promptId;

    /** 聊天类型 */
    private String chatType;

    /** 提示词内容 */
    private String promptContent;

}
