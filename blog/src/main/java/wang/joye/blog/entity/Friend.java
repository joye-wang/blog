package wang.joye.blog.entity;

import wang.joye.blog.controller.action.UpdateAction;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 友情链接
 *
 * @author 天宇
 * @since 2018-09-05
 */
@Data
public class Friend {
    @TableId(type = IdType.AUTO)
    @NotNull(groups = UpdateAction.class)
    private Integer id;
    @NotBlank
    private String link;
    @NotBlank
    private String name;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
