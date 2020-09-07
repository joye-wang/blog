package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.annotation.RequestAuthentication;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Post;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.service.CategoryService;
import wang.joye.blog.service.PostService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@RestController
@RequestMapping("posts")
@RequestAuthentication
@Validated
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 管理员获取文章列表
     * 可以获取隐藏的文章
     */
    @GetMapping
    public Object listPost(@RequestParam(required = false) String search,
                           @RequestParam(required = false) Long categoryId, Page<Post> page) {
        return postService.listPost(search, categoryId, page, true);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("{id}")
    public Post getPost(@PathVariable @NotNull Long id) {
        return postService.getBaseMapper().getPostByAdmin(id);
    }

    /**
     * 添加文章后返回主键
     */
    @PostMapping
    public long addPost(@Validated Post post) throws BusinessException {
        int count = postService.count(Wrappers.<Post>lambdaQuery().eq(Post::getTitle, post.getTitle()));
        if (count > 0)
            throw new BusinessException("文章标题已存在");
        categoryService.checkCategory(post.getCategoryId());

        return postService.insertPost(post);
    }

    @DeleteMapping("{id}")
    public void deletePost(@PathVariable @NotNull Long id) {
        postService.deleteArticle(id);
    }

    @PutMapping
    public void updatePost(@Validated({UpdateAction.class, Default.class}) Post post) {
        categoryService.checkCategory(post.getCategoryId());
        postService.updatePost(post);
    }

    /**
     * 移动文章到新的目录
     */
    @PutMapping("category")
    public void moveArticle(@NotNull Long postId, @NotNull @Min(1) Long newCategoryId) {
        categoryService.checkCategory(newCategoryId);
        postService.updatePostCategory(postId, newCategoryId);
    }

    @PutMapping("status")
    public void toggleStatus(@NotNull Long postId, @NotNull Integer status) {
        postService.update(new UpdateWrapper<Post>().lambda()
                .set(Post::getStatus, status)
                .set(Post::getUpdateTime, LocalDateTime.now())
                .eq(Post::getId, postId));
    }
}