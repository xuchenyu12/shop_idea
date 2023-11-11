package com.soft2242.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.soft2242.shop.entity.User;
import com.soft2242.shop.query.UserLoginQuery;
import com.soft2242.shop.vo.LoginResultVO;
import com.soft2242.shop.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {

    //登录
    LoginResultVO login(UserLoginQuery query);


    /**
     * 根据UserId查找用户
     * @param userId
     * @return
     */
    User getUserInfo(Integer userId);

    /**
     * 修改用户信息
     * @param userVO
     * @return
     */
    UserVO editUserInfo(UserVO userVO);

    String editUserAvatar(Integer userId, MultipartFile file);
}