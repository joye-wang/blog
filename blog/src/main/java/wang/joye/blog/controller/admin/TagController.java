package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.PostTag;
import wang.joye.blog.entity.Tag;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.service.PostTagService;
import wang.joye.blog.service.TagService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@RestController
@RequestMapping("tags")
@Validated
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private PostTagService articleTagService;

    @GetMapping
    public List<Tag> listTag() {
        return tagService.list();
    }

    @PostMapping
    public void insertTag(@Validated(CreateAction.class) @RequestBody Tag tag) {
        int count = tagService.count(new QueryWrapper<Tag>().lambda().eq(Tag::getName, tag.getName()));
        if (count > 0) {
            throw new BusinessException("标签名称已经存在");
        }
        tagService.save(tag);
    }

    @DeleteMapping("{id}")
    public void deleteTag(@PathVariable @NotNull Long id) {
        tagService.removeById(id);
    }

    @PutMapping
    public void updateTag(@Validated(UpdateAction.class) @RequestBody Tag tag) {
        // 更新关联中的冗余字段
        articleTagService.update(new UpdateWrapper<PostTag>().lambda().set(PostTag::getTagName, tag.getName()).eq(PostTag::getTagId, tag.getTagId()));
        tagService.updateById(tag);
    }
}

