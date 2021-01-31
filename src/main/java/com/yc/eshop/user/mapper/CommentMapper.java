package com.yc.eshop.user.mapper;

import com.yc.eshop.common.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    void insertComment(Comment comment);

    void updateOrderIsComment(String orderId);

}
