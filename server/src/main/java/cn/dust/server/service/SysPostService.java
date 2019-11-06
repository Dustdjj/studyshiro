package cn.dust.server.service;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/13 15:28
 */
public interface SysPostService extends IService<SysPost> {

    PageUtil queryPage(Map<String,Object> params);

    void savePost(SysPost sysPost);

    void updatePost(SysPost sysPost);

    void deletePatch(Long[] ids);
}
