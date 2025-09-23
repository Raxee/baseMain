package com.ruoyi.common.core.domain.entity;

import lombok.Data;

@Data
public class ChatRequest {
    /**
     * 用户发送的信息
     */
    private String userMsg;

    /**
     * 聊天类型
     */
    private String chatType;

}
