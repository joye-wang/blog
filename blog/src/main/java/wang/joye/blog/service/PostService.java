package wang.joye.blog.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.joye.blog.entity.Category;
import wang.joye.blog.entity.Post;
import wang.joye.blog.entity.PostTag;
import wang.joye.blog.mapper.PostMapper;

import java.time.LocalDateTime;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Service
@Slf4j
public class PostService extends ServiceImpl<PostMapper, Post> {

    private final CategoryService categoryService;
    private final PostTagService postTagService;
    private final PostMapper postMapper;

    public PostService(CategoryService categoryService, PostTagService postTagService, PostMapper postMapper) {
        this.categoryService = categoryService;
        this.postTagService = postTagService;
        this.postMapper = postMapper;
    }

    /**
     * 添加文章，返回主键
     *
     * @return 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertPost(Post post) {

        // 获取文章目录的名字，同时使category中的count+1
        if (post.getCategoryId() != null) {
            Category category = categoryService.getById(post.getCategoryId());
            if (category != null) {
                post.setCategoryName(category.getName());
                categoryService.increasePostCount(post.getCategoryId());
            }
        }

        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(post.getCreateTime());
        post.setPageViews(0L);
        save(post);

        if (CollUtil.isNotEmpty(post.getTagIds())) {
            postTagService.batchInsertPostTag(post.getId(), post.getTagIds());
        }
        return post.getId();
    }

    public void increasePageViews(long postId) {
        update(Wrappers.<Post>lambdaUpdate().setSql("page_views=page_views+1").eq(Post::getId, postId));
    }

    public void updatePost(Post post) {
        // 取文章的之前数据
        Post dbData = getOne(new QueryWrapper<Post>().lambda().eq(Post::getId, post.getId())
                .select(Post::getCategoryId, Post::getPageViews));
        // 之前的目录，文章量减1
        if (dbData.getCategoryId() != null)
            categoryService.reducePostCount(dbData.getCategoryId());

        // 如果清除了文章归档
        if (post.getCategoryId() == null) {
            post.setCategoryName(null);
        } else if (!dbData.getCategoryId().equals(post.getCategoryId())) {
            // 修改文章目录为新归档的名字
            Category newCategory = categoryService.getById(post.getCategoryId());
            post.setCategoryName(newCategory.getName());
            // 新目录文章加1
            categoryService.increasePostCount(post.getCategoryId());
        }

        // 更新文章标签
        postTagService.updatePostTags(post.getId(), post.getTagIds());

        // 更新文章数据
        post.setCreateTime(null);
        post.setPageViews(null);
        post.setUpdateTime(LocalDateTime.now());
        update(post, Wrappers.<Post>lambdaUpdate().eq(Post::getId, post.getId()));
    }

    /**
     * 修改文章所在的目录
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePostCategory(long postId, Long newCategoryId) {
        // 取文章的之前数据
        Post dbPost = getOne(new QueryWrapper<Post>().lambda().eq(Post::getId, postId)
                .select(Post::getCategoryId, Post::getId));
        if (dbPost == null)
            return;

        if (dbPost.getCategoryId() != null) {
            categoryService.reducePostCount(dbPost.getCategoryId());
        }
        if (newCategoryId != null) {
            // 修改文章目录为新归档的名字
            Category newCategory = categoryService.getById(newCategoryId);
            update(Wrappers.<Post>lambdaUpdate()
                    .set(Post::getCategoryId, newCategoryId)
                    .set(Post::getCategoryName, newCategory.getName())
                    .eq(Post::getId, postId));
            // 新目录文章+1
            categoryService.increasePostCount(newCategoryId);
        }
    }

    /**
     * @param showHidden 是否展示被隐藏的文章
     */
    public Page<Post> listPost(String search, Long categoryId, Page<Post> page, boolean showHidden) {
        Page<Post> res = new Page<>();
        res.setTotal(postMapper.countPost(search, categoryId, showHidden));
        if (res.getTotal() > 0) {
            // Pagination是继承RowBounds，是mybatis内置对象，无法在mapper层获得获得其属性
            res.setRecords(postMapper.listPost(search, categoryId, (page.getCurrent() - 1) * page.getSize(), page.getSize(), showHidden));
        }
        return res;
    }

    public void deleteArticle(long articleId) {
        // 删除标签
        postTagService.remove(new QueryWrapper<PostTag>().lambda().eq(PostTag::getPostId, articleId));
        // 减少对应系列的文章数量
        // select中必须传入articleId，否则，若archiveId为空，article则为null，触发NPE
        Post article = getOne(new QueryWrapper<Post>().lambda().eq(Post::getId, articleId).select(Post::getCategoryId, Post::getId));
        if (article.getCategoryId() != null) {
            categoryService.reducePostCount(article.getCategoryId());
        }
        // 最后删除文章
        removeById(articleId);
    }

    /**
     * 使文章访问量+1
     */
    private void increaseArticlePageViews(long articleId) {
        this.update(new Post(), new UpdateWrapper<Post>().setSql("page_views=page_views+1").eq("article_id", articleId));
    }
}
