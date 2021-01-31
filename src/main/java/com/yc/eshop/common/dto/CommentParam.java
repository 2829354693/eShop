package com.yc.eshop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentParam implements Serializable {

    private MultipartFile[] pics;

    @NotNull
    private String orderId;

    @NotNull
    private Integer userId;

    private Integer score;

    private String commentContent;

}
