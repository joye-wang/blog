package wang.joye.blog.service;

import wang.joye.blog.entity.Category;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.mapper.ArchiveMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Service
public class CategoryService extends ServiceImpl<ArchiveMapper, Category> {

    public void checkCategory(Long categoryId) {
        if (categoryId == null)
            return;
        int count = count(Wrappers.<Category>lambdaQuery().eq(Category::getId, categoryId));
        if (count == 0)
            throw new BusinessException("目录不存在");
    }

    /**
     * 文章数量+1
     */
    public void increasePostCount(long categoryId) {
        update(new UpdateWrapper<Category>().lambda().setSql("post_count=post_count+1").eq(Category::getId, categoryId));
    }

    /**
     * 文章数量 -1
     */
    public void reducePostCount(long categoryId) {
        update(new UpdateWrapper<Category>().lambda().setSql("post_count=post_count-1")
                .eq(Category::getId, categoryId).gt(Category::getPostCount, 1));
    }
}
