package com.yc.eshop.user.service.impl;

import com.yc.eshop.common.dto.CommentParam;
import com.yc.eshop.common.entity.Comment;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import com.yc.eshop.user.mapper.CommentMapper;
import com.yc.eshop.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {

    @Value("${comment-pic-save-path}")
    private String commentPicSavePath;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ApiResponse<?> commentCommit(CommentParam commentParam) throws IOException {
        MultipartFile[] pics = commentParam.getPics();
        String orderId = commentParam.getOrderId();
        Integer userId = commentParam.getUserId();
        Integer score = commentParam.getScore();
        String commentContent = commentParam.getCommentContent();

        if (pics.length == 0) {
            Comment comment = Comment.builder()
                    .content(commentContent)
                    .rate(score)
                    .time(new Date())
                    .orderId(orderId)
                    .userId(userId)
                    .build();
            commentMapper.insertComment(comment);
        } else {
            StringBuilder pictureStr = new StringBuilder();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");
            for (int i = 0; i < pics.length; i++) {
                String originalFilename = pics[i].getOriginalFilename();
                if (Objects.isNull(originalFilename) || originalFilename.length() == 0) {
                    return ApiResponse.failure(ResponseCode.NOT_ACCEPTABLE, "图片名称不能为空！");
                }
                String newPicName = df.format(new Date())+"_cPic_"+userId+"_"+i+originalFilename.substring(originalFilename.lastIndexOf("."));
                File cPic = new File(commentPicSavePath + newPicName);
                pics[i].transferTo(cPic);
                pictureStr.append("comment_pic/").append(newPicName).append(",");
            }
            String picture = pictureStr.deleteCharAt(pictureStr.length() - 1).toString();
            Comment comment = Comment.builder()
                    .picture(picture)
                    .content(commentContent)
                    .rate(score)
                    .time(new Date())
                    .orderId(orderId)
                    .userId(userId)
                    .build();
            commentMapper.insertComment(comment);
        }
        commentMapper.updateOrderIsComment(orderId);
        return ApiResponse.ok();
    }
}
