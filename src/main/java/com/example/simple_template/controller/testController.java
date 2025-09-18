package com.example.simple_template.controller;

import com.example.simple_template.common.util.R;
import com.example.simple_template.db.dto.TestSayHello;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 
 */
@RestController
@RequestMapping("/test")
@Tag(name = "testController", description = "测试web接口")
public class testController {


    @PostMapping("/sayHello")
    @Operation(summary = "最简单的测试方法")
    public R sayHello(@Valid @RequestBody TestSayHello form) {
        return R.ok().put("message", "Hello," + form.getName());
    }

}
