package wang.joye.blog.controller.admin;

import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Category;
import wang.joye.blog.entity.Post;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.service.CategoryService;
import wang.joye.blog.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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

    private final CategoryService archiveService;
    private final PostService articleService;

    public CategoryController(CategoryService archiveService, PostService articleService) {
        this.archiveService = archiveService;
        this.articleService = articleService;
    }

    @GetMapping
    public List<Category> listArchives() {
        return archiveService.list(new QueryWrapper<Category>().lambda().orderByDesc(Category::getCreateTime));
    }

    @GetMapping("{archiveId}")
    public Category getArchive(@NotNull @PathVariable String archiveId) {
        return archiveService.getById(archiveId);
    }

    @PostMapping("create")
    public void addArchive(@Validated(CreateAction.class) Category archive) throws BusinessException {
        int count = archiveService.count(new QueryWrapper<Category>().lambda().eq(Category::getName, archive.getName()));
        if (count > 0) {
            throw new BusinessException("系列名称已经存在");
        }
        archiveService.save(archive);
    }

    @DeleteMapping("delete")
    public void deleteArchive(@NotNull Long archiveId) {
        archiveService.removeById(archiveId);
        // 删除archive后，把文章的系列也置为null
        articleService.update(new UpdateWrapper<Post>().lambda().eq(Post::getCategoryId, archiveId).
                set(Post::getCategoryId, null).set(Post::getCategoryName, null));
    }

    @PutMapping("update")
    public void updateArchive(@Validated(UpdateAction.class) Category archive) {
        // 只更新archive的name
        archiveService.update(new UpdateWrapper<Category>().lambda().set(Category::getName, archive.getName())
                .set(Category::getUpdateTime, LocalDateTime.now())
                .eq(Category::getId, archive.getId()));
        // 更新文章表的冗余字段
        articleService.update(new UpdateWrapper<Post>().lambda().set(Post::getCategoryName, archive.getName()).eq(Post::getCategoryId, archive.getId()));
    }
}

