package wang.joye.blog.service;

import wang.joye.blog.mapper.PostTagMapper;
import wang.joye.blog.entity.PostTag;
import wang.joye.blog.entity.Tag;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Service
public class PostTagService extends ServiceImpl<PostTagMapper, PostTag> {

    @Autowired
    private TagService tagService;

    public void batchInsertPostTag(int postId, List<Integer> postTagIds) {
        Collection<Tag> tags = tagService.listByIds(postTagIds);
        List<PostTag> articleTags = tags.stream().map(tag -> {
            PostTag articleTag = new PostTag();
            articleTag.setPostId(postId);
            articleTag.setTagId(tag.getTagId());
            articleTag.setTagName(tag.getName());
            return articleTag;
        }).collect(Collectors.toList());
        saveBatch(articleTags);
    }

    /**
     * 更新文章标签，先把原来的标签删掉，再把新标签插入
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePostTags(int postId, List<Integer> postTagsId) {
        this.remove(Wrappers.<PostTag>lambdaQuery().eq(PostTag::getPostId, postId));
        batchInsertPostTag(postId, postTagsId);
    }
}
