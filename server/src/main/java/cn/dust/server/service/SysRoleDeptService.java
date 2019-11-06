package cn.dust.server.service;

import cn.dust.model.entity.SysRoleDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: dust
 * @Date: 2019/10/17 13:50
 */
public interface SysRoleDeptService extends IService<SysRoleDept> {

    void saveOrUpdate(Long roleId, List<Long> deptIdList);

    void deleteBatch(List<Long> roleIds);

    List<Long> queryDeptIdList(Long roleId);

}
