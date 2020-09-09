package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Friend;
import wang.joye.blog.service.FriendService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @since 2018-08-13
 */
@RestController
@RequestMapping("friends")
@Validated
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping
    public List<Friend> list() {
        return friendService.list();
    }

    @PostMapping
    public void insertFriend(@Validated(CreateAction.class) @RequestBody Friend friend) {
        friend.setCreateTime(LocalDateTime.now());
        friend.setUpdateTime(friend.getCreateTime());
        friendService.save(friend);
    }

    @DeleteMapping("{id}")
    public void deleteFriend(@PathVariable @NotNull Long id) {
        friendService.removeById(id);
    }

    @PutMapping
    public void updateFriend(@Validated(UpdateAction.class) @RequestBody Friend friend) {
        friend.setCreateTime(null);
        friend.setUpdateTime(LocalDateTime.now());

        friendService.update(friend, Wrappers.<Friend>lambdaUpdate()
                .eq(Friend::getId, friend.getId())
        );
    }
}

