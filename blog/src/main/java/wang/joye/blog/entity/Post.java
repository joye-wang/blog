package wang.joye.blog.entity;

import wang.joye.blog.controller.action.UpdateAction;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Data
public class Post {

    public static final int SHOW = 1;
    public static final int NOT_SHOW = 0;

    // 指定mybatis-plus使用数据库自增主键
    @TableId(type = IdType.AUTO)
    @NotNull(groups = UpdateAction.class)
    private Integer id;
    @NotBlank
    private String title;
    @NotBlank
    private String mdContent;
    @NotBlank
    private String content;
    private Long categoryId;
    private String categoryName;
    /**
     * 状态, 0隐藏，1展示
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
    private Long pageViews;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 更新接收字段
     */
    @TableField(exist = false)
    private List<Integer> tagIds;
    /**
     * 前台展示字段
     */
    @TableField(exist = false)
    private List<PostTag> tags;
}
