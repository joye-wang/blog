package wang.joye.blog.entity;

import wang.joye.blog.controller.action.UpdateAction;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Data
public class Tag {

    @TableId(type = IdType.AUTO)
    @NotNull(groups = UpdateAction.class)
    private Integer tagId;
    @NotBlank
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
