package com.soft2242.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressDeleteFlagEnum {
    NOT_DELETE_ADDRESS(0, "未删除"),
    DELETE_ADDRESS(1, "已删除");
    private final Integer value;
    private final String name;
}