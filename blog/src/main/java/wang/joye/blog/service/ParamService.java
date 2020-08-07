package wang.joye.blog.service;

import wang.joye.blog.entity.Param;
import wang.joye.blog.mapper.ParamMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 天宇
 * @since 2018-08-13
 */
@Service
public class ParamService extends ServiceImpl<ParamMapper, Param> {

    public String getValue(String key) {
        Param info = getOne(Wrappers.<Param>lambdaQuery().eq(Param::getK, key));
        if (info == null)
            return "";
        return info.getVal();
    }

    /**
     * 得到页面的脚注信息
     * 域名备案号
     */
    public Map<String, String> getFooter() {
       Map<String,String> map = new HashMap<>(2);
       map.put("domain", getValue("domain"));
       map.put("icp", getValue("icp"));
       return map;
    }
}
