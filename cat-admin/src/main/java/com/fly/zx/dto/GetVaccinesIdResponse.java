package com.fly.zx.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @author zx
 * @date 2021/7/13 14:25
 */
@Data
public class GetVaccinesIdResponse {
    private Integer status;
    private Integer id;
    private List<VaccinesInfoDto> list;
}
