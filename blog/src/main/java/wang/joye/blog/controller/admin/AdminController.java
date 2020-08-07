package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.joye.blog.annotation.RequestAuthentication;
import wang.joye.blog.entity.Admin;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.global.Constants;
import wang.joye.blog.service.AdminService;
import wang.joye.blog.util.TResult;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author 天宇
 * @since 2018-09-05
 */
@RestController
@RequestMapping("admins")
@Validated
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final HttpSession session;

    public AdminController(AdminService adminService, HttpSession session) {
        this.adminService = adminService;
        this.session = session;
    }

    @PostMapping("login")
    public void login(@NotBlank String username, @NotBlank String password) {
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        int count = adminService.count(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getId, Admin.ADMIN_ID)
                .eq(Admin::getUsername, username).eq(Admin::getPwd, md5Pwd));
        if (count == 0) {
            throw new BusinessException(TResult.TResultCode.USER_LOGIN_ERROR.getMessage());
        }
        session.setAttribute(Constants.SESSION_KEY_ADMIN_ID, Admin.ADMIN_ID);
    }

    @RequestMapping("logout")
    @RequestAuthentication
    public void logout() {
        session.invalidate();
    }

    @PostMapping("resetPwd")
    @RequestAuthentication
    public void resetPwd(@NotBlank String oldPwd, @NotBlank @Length(min = 6) String newPwd) {

        String md5OldPwd = DigestUtils.md5DigestAsHex(oldPwd.getBytes());
        if (!adminService.getById(Admin.ADMIN_ID).getPwd().equals(md5OldPwd)) {
            throw new BusinessException("密码错误");
        }

        String md5NewPwd = DigestUtils.md5DigestAsHex(newPwd.getBytes());
        adminService.update(new UpdateWrapper<Admin>().lambda()
                .set(Admin::getPwd, md5NewPwd)
                .set(Admin::getUpdateTime, LocalDateTime.now()));
    }
}