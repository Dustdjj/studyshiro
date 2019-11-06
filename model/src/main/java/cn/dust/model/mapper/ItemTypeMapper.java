package cn.dust.model.mapper;

import cn.dust.model.entity.ItemType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ItemTypeMapper extends BaseMapper<ItemType> {
    int deleteByPrimaryKey(Integer typeId);

    int insert(ItemType record);

    int insertSelective(ItemType record);

    ItemType selectByPrimaryKey(Integer typeId);

    int updateByPrimaryKeySelective(ItemType record);

    int updateByPrimaryKey(ItemType record);
}