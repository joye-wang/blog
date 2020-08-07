package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Friend;
import wang.joye.blog.exception.BusinessException;
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
    public void insertFriend(@Validated(CreateAction.class) Friend friend) throws BusinessException {
        friendService.save(friend);
    }

    @DeleteMapping("{id}")
    public void deleteFriend(@PathVariable @NotNull Long id) {
        friendService.removeById(id);
    }

    @PutMapping
    public void updateFriend(@Validated(UpdateAction.class) Friend friend) {
        friendService.update(new UpdateWrapper<Friend>().lambda()
                .set(Friend::getName, friend.getName())
                .set(Friend::getLink, friend.getLink())
                .set(Friend::getUpdateTime, LocalDateTime.now())
                .eq(Friend::getId, friend.getId())
        );
    }
}

