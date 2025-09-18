package com.example.emos.wx.controller;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.controller.form.TestSayHello;
import com.example.emos.wx.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/test")
@Api("测试web接口")
public class testController {

    @Autowired
    TodoService todoService;

    @PostMapping("/sayHello")
    @ApiOperation("最简单的测试方法")
    public R sayHello(@Valid @RequestBody TestSayHello form) {
        return R.ok().put("message", "Hello," + form.getName());
    }

    @GetMapping("/scheduleTask")
    @ApiOperation("定时改变任务状态")
    public R scheduleTask() {
        // 获取今天的日期（年月日）
        LocalDate today = LocalDate.now();
        todoService.changeTodoToNotFinished(today);
        return R.ok().put("message", "同步成功了吗？");
    }
}
