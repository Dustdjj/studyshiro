package cn.dust.server.service;

import cn.dust.model.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/14 19:47
 */
public interface SysDeptService extends IService<SysDept> {

    List<SysDept> queryAll(Map<String,Object> map);

    List<Long> queryDeptIds(Long parentId);

    List<Long> getSubDeptIdList(Long deptId);
}
