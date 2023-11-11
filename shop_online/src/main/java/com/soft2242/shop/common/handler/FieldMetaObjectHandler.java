package com.soft2242.shop.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.soft2242.shop.common.constant.Constant;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        //        创建时间
        this.strictInsertFill(metaObject, Constant.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
//        更新时间
        this.strictInsertFill(metaObject, Constant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
//        逻辑删除
        this.strictInsertFill(metaObject, Constant.DELETE_FLAG, Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        更新时间
        this.strictUpdateFill(metaObject, Constant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());

    }
}
