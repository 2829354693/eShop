package com.yc.eshop.user.controller;


import com.yc.eshop.common.dto.CommentParam;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.user.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@Api(value = "/comment", tags = "评价Controller")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ApiOperation("用户提交订单评价")
    @PostMapping("/commentCommit")
    public ApiResponse<?> commentCommit(@RequestParam("pics")MultipartFile[] pics,String orderId,Integer userId,Integer score,String commentContent) throws IOException {
        CommentParam commentParam = CommentParam.builder()
                .pics(pics)
                .orderId(orderId)
                .userId(userId)
                .score(score)
                .commentContent(commentContent)
                .build();
        return commentService.commentCommit(commentParam);
    }




}
