package com.soft2242.shop.controller;

import com.soft2242.shop.common.result.Result;
import com.soft2242.shop.common.utils.JWTUtils;
import com.soft2242.shop.constant.APIConstant;
import com.soft2242.shop.entity.User;
import com.soft2242.shop.query.UserLoginQuery;
import com.soft2242.shop.service.UserService;
import com.soft2242.shop.vo.LoginResultVO;

import com.soft2242.shop.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.Map;

import static com.soft2242.shop.common.utils.ObtainUserIdUtils.getUserId;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuchenyu12
 * @since 2023-11-08
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping("user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "微信登录")
    @PostMapping("login/wxMin")
    public Result<LoginResultVO> wxLogin(@RequestBody @Validated UserLoginQuery query) {
        LoginResultVO userVO = userService.login(query);
        return Result.ok(userVO);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/profile")
    private Result<User> getUserInfo(HttpServletRequest request) {
        Integer userId = getUserId(request);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>userId:"+userId);
        User userInfo = userService.getUserInfo(userId);
        return Result.ok(userInfo);
    }
    @Operation(summary = "修改用户信息")
    @PutMapping("/profile")
    private Result<UserVO> editUserInfo(HttpServletRequest request, @RequestBody @Validated UserVO userVO) {
        Integer userId = getUserId(request);
        userVO.setId(userId);
        UserVO userInfo = userService.editUserInfo(userVO);
        return Result.ok(userInfo);
    }
    @Operation(summary = "修改用户头像")
    @PostMapping("/profile/avatar")
    private Result<String> editUserAvatar(HttpServletRequest request, MultipartFile file) {
        Integer userId = getUserId(request);
        String uploadFileName = userService.editUserAvatar(userId, file);
        return Result.ok(uploadFileName);
    }
}