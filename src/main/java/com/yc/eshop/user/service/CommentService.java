package com.yc.eshop.user.service;

import com.yc.eshop.common.dto.CommentParam;
import com.yc.eshop.common.response.ApiResponse;

import java.io.IOException;

public interface CommentService {

    ApiResponse<?> commentCommit(CommentParam commentParam) throws IOException;

}
