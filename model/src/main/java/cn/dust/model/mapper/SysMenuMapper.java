package cn.dust.model.mapper;

import cn.dust.model.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    //根据父级Id，查询子菜单
    List<SysMenu> queryListParentId(Long parentId);

    //获取不包含按钮的菜单列表
    List<SysMenu> queryNotButtonList();

    //获取所有菜单
    List<SysMenu> queryList();
}