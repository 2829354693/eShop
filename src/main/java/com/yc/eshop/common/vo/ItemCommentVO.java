package com.yc.eshop.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCommentVO {
    private String headPic;

    private String nickname;

    private String sex;

    private String content;

    private String picture;

    private String[] pictures;

    private Integer rate;

    private Date time;

    private String timeStr;

}
