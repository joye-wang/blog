package wang.joye.blog.controller.blog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wang.joye.blog.entity.Post;
import wang.joye.blog.service.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面渲染控制器
 * 用于SEO优化
 *
 * @author joye
 * @since 2018-08-13
 */
@Controller
@RequestMapping
@Validated
@Slf4j
public class ViewController {

    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ParamService blogInfoService;
    @Autowired
    private FriendService friendService;

    /**
     * 博客主页
     */
    @GetMapping({"/"})
    public ModelAndView index(@RequestParam(required = false) String search,
                              @RequestParam(required = false) Long categoryId, Page<Post> page) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("data", postService.listPost(search, categoryId, page, false));
        mav.addObject("tags", tagService.list());
        mav.addObject("headImgUrl", blogInfoService.getValue("headImgUrl"));
        mav.addObject("nickname", blogInfoService.getValue("nickname"));
        mav.addObject("motto", blogInfoService.getValue("motto"));
        mav.addObject("friends", friendService.list());
        mav.addAllObjects(blogInfoService.getFooter());
        return mav;
    }

    /**
     * 获取文章详情页面
     */
    @GetMapping("post/{postId}")
    public ModelAndView getArticleById(@PathVariable @NotNull Long postId) {
        ModelAndView mav = new ModelAndView("post");
        mav.addObject("post", postService.getBaseMapper().getPostByUser(postId));
        postService.increasePageViews(postId);
        mav.addAllObjects(blogInfoService.getFooter());
        return mav;
    }

    /**
     * 目录页
     */
    @GetMapping("category")
    public ModelAndView category() {
        ModelAndView mav = new ModelAndView("category");
        mav.addObject("categories", categoryService.list());
        mav.addAllObjects(blogInfoService.getFooter());
        return mav;
    }

    @GetMapping("about")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("about");
        Map<String, Object> map = new HashMap<>();
        blogInfoService.list().forEach(i -> {
            map.put(i.getK(), i.getVal());
        });
        mav.addObject("info", map);
        mav.addObject("friends", friendService.list());
        mav.addAllObjects(blogInfoService.getFooter());
        return mav;
    }
}