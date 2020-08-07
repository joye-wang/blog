package wang.joye.blog.entity;

import wang.joye.blog.controller.action.UpdateAction;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 文章所属系列，文章分类
 *
 * @author 天宇
 * @since 2018-08-13
 */
@Data
public class Category {

    @TableId(type = IdType.AUTO)
    @NotNull(groups = {UpdateAction.class}, message = "必须指定archive id")
    private Integer id;
    @NotBlank
    private String name;
    private Integer postCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
