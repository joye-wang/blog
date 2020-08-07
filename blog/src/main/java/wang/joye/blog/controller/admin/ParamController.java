package wang.joye.blog.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wang.joye.blog.controller.action.CreateAction;
import wang.joye.blog.controller.action.UpdateAction;
import wang.joye.blog.entity.Param;
import wang.joye.blog.exception.BusinessException;
import wang.joye.blog.service.ParamService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @since 2018-09-05
 */
@RestController
@RequestMapping("params")
public class ParamController {

    private final ParamService paramService;

    public ParamController(ParamService paramService) {
        this.paramService = paramService;
    }

    @GetMapping
    public List<Param> listBlogInfo() {
        return paramService.list();
    }

    @PostMapping
    public void insert(@Validated({CreateAction.class, Default.class}) Param param) {
        int count = paramService.count(Wrappers.<Param>lambdaQuery().eq(Param::getK, param.getK()));
        if (count > 0) {
            throw new BusinessException("相同key已存在");
        }
        param.setCreateTime(LocalDateTime.now());
        param.setUpdateTime(param.getCreateTime());
        paramService.save(param);
    }

    @PutMapping
    public void updateValue(@Validated({Default.class, UpdateAction.class}) Param param) {
        param.setCreateTime(null);
        param.setUpdateTime(LocalDateTime.now());
        paramService.update(param, Wrappers.<Param>lambdaUpdate().eq(Param::getId, param.getId()));
    }

    @GetMapping("{key}")
    public String getValue(@PathVariable @NotBlank String key) {
        return paramService.getValue(key);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull Long id) {
        paramService.removeById(id);
    }
}

