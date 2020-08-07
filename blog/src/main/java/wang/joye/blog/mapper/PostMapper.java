package wang.joye.blog.mapper;

import wang.joye.blog.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Repository
public interface PostMapper extends BaseMapper<Post> {

    List<Post> listPost(@Param("search") String search, @Param("categoryId") Long categoryId,
                        @Param("offset") long offset, @Param("rows") long rows, @Param("showHidden") boolean showHidden);

    int countPost(@Param("search") String search, @Param("categoryId") Long categoryId, @Param("showHidden") boolean showHidden);

    Post getPostByAdmin(@Param("id") long id);

    Post getPostByUser(@Param("id") long id);
}
