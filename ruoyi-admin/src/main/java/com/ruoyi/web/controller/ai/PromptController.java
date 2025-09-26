package com.ruoyi.web.controller.ai;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysPrompt;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prompt")
public class PromptController extends BaseController {

    @Autowired
    private ISysPromptService sysPromptService;

    /**
     * 查询系统提示词列表
     *
     * @param sysPrompt 系统提示词查询条件对象
     * @return 系统提示词列表数据
     */
    @GetMapping("/list")
    public TableDataInfo list(SysPrompt sysPrompt) {
        List<SysPrompt> sysPrompts = sysPromptService.selectPromptList(sysPrompt);
        return getDataTable(sysPrompts);
    }

    /**
     * 获取提示信息
     *
     * @param promptId 提示ID，可选参数
     * @return AjaxResult 包含提示信息的响应结果
     */
    @GetMapping(value = { "/", "/{promptId}" })
    public AjaxResult getInfo(@PathVariable(value = "promptId", required = false) Long promptId) {
        return success(sysPromptService.selectPromptById(promptId));
    }

    /**
     * 添加系统提示信息
     *
     * @param sysPrompt 系统提示对象，包含需要添加的提示信息
     * @return AjaxResult 操作结果，包含操作状态和相关信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysPrompt sysPrompt) {
        return toAjax(sysPromptService.insertPrompt(sysPrompt));
    }

    /**
     * 修改系统提示信息
     *
     * @param sysPrompt 系统提示对象，包含需要修改的提示信息
     * @return AjaxResult 操作结果，包含操作状态和相关信息
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPrompt sysPrompt) {
        return toAjax(sysPromptService.updatePrompt(sysPrompt));
    }

    /**
     * 删除系统提示信息
     *
     * @param promptIds 需要删除的提示ID列表
     * @return AjaxResult 操作结果，包含操作状态和相关信息
     */
    @DeleteMapping("/{promptIds}")
    public AjaxResult remove(@PathVariable Long[] promptIds) {
        return toAjax(sysPromptService.deletePromptByIds(promptIds));
    }

}
