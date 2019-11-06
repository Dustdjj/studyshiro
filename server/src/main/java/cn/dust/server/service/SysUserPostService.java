package cn.dust.server.service;

import cn.dust.model.entity.SysUserPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/17 22:29
 */
public interface SysUserPostService extends IService<SysUserPost> {

    String getPostNameByUserId(Long userId);

    void saveOrUpdate(Long userId, List<Long> postIds);

    List<Long> queryPostIdList(Long userId);
}