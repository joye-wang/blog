package wang.joye.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 天宇
 */
@Data
public class PostTag {
    @TableId
    private Integer postId;
    @TableId
    private Integer tagId;
    /**
     * 标签名称
     */
    private String tagName;
}
