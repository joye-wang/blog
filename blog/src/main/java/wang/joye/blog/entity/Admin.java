package wang.joye.blog.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * “关于本站”页面的信息
 *
 * @author 天宇
 * @since 2018-09-05
 */
@Data
public class Admin {

    public static final int ADMIN_ID = 1;

    @TableId
    private Integer id;

    private String username;
    private String pwd;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}