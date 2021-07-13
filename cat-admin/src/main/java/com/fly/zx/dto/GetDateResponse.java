package com.fly.zx.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @author zx
 * @date 2021/7/13 13:57
 */
@Data
public class GetDateResponse {
    private Integer status;
    private List<DateDto> list;
}
