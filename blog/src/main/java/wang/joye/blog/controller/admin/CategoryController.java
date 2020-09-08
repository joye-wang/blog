package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Category;
import wang.joye.blog.entity.Post;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.service.CategoryService;
import wang.joye.blog.service.PostService;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Validated
@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Category> listCategory() {
        return categoryService.list(new QueryWrapper<Category>().lambda().orderByDesc(Category::getCreateTime));
    }

    @GetMapping("{id}")
    public Category getCategory(@NotNull @PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public void addArchive(@Validated({CreateAction.class, Default.class}) @RequestBody Category category) throws BusinessException {
        int count = categoryService.count(new QueryWrapper<Category>().lambda().eq(Category::getName, category.getName()));
        if (count > 0) {
            throw new BusinessException("系列名称已经存在");
        }
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(category.getCreateTime());
        categoryService.save(category);
    }

    @DeleteMapping("{id}")
    public void deleteArchive(@PathVariable @NotNull Long id) {
        categoryService.removeById(id);
        // 删除archive后，把文章的系列也置为null
        postService.update(new UpdateWrapper<Post>().lambda().eq(Post::getCategoryId, id).
                set(Post::getCategoryId, null).set(Post::getCategoryName, null));
    }

    @PutMapping
    public void updateArchive(@Validated({UpdateAction.class, Default.class}) @RequestBody Category category) {
        // 只更新archive的name
        categoryService.update(new UpdateWrapper<Category>().lambda().set(Category::getName, category.getName())
                .set(Category::getUpdateTime, LocalDateTime.now())
                .eq(Category::getId, category.getId()));
        // 更新文章表的冗余字段
        postService.update(new UpdateWrapper<Post>().lambda().set(Post::getCategoryName, category.getName()).eq(Post::getCategoryId, category.getId()));
    }
}