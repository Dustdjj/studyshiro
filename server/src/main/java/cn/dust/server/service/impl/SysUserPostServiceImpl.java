package cn.dust.server.service.impl;

import cn.dust.common.utils.PageUtil;
import cn.dust.model.entity.SysPost;
import cn.dust.model.entity.SysUserPost;
import cn.dust.model.mapper.SysUserPostMapper;
import cn.dust.server.service.SysPostService;
import cn.dust.server.service.SysUserPostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: dust
 * @Date: 2019/10/17 22:30
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

    @Override
    public String getPostNameByUserId(Long userId) {
        //第一种写法
        /*Set<String> set=baseMapper.getPostNamesByUserId(userId);
        if (set!=null && !set.isEmpty()){
            return Joiner.on(",").join(set);
        }else{
            return "";
        }*/

        //传统的写法
        /*StringBuilder sb=new StringBuilder("");
        List<SysUserPostEntity> list=baseMapper.getByUserId(userId);
        if (list!=null && !list.isEmpty()){
            for (SysUserPostEntity entity:list){
                sb.append(entity.getPostName()).append(",");
            }
        }
        String result=sb.toString();
        if (result.lastIndexOf(",")>=0){
            result=result.substring(0,result.lastIndexOf(","));
        }
        return result;*/

        String result="";

        List<SysUserPost> list=baseMapper.getByUserId(userId);
        if (list!=null && !list.isEmpty()){
            //java8的stream api
            Set<String> set=list.stream().map(SysUserPost::getPostName).collect(Collectors.toSet());
            result= Joiner.on(",").join(set);
        }
        return result;
    }

    //维护用户~岗位的关联关系
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long userId, List<Long> postIds) {
        try{
            //需要先清除旧的关联数据，再插入新的关联信息
            this.remove(new QueryWrapper<SysUserPost>().eq("user_id",userId));
        }catch (Exception e){
            System.out.println("33333333333333333333333333333333333333");
        }


        if (postIds!=null && !postIds.isEmpty()){
            SysUserPost sysUserPost;
            for (Long pId:postIds){
                sysUserPost=new SysUserPost();
                sysUserPost.setPostId(pId);
                sysUserPost.setUserId(userId);
                this.save(sysUserPost);
            }
        }

    }

    //获取用户分配的岗位列表信息
    @Override
    public List<Long> queryPostIdList(Long userId) {
        return baseMapper.queryPostIdList(userId);
    }
}
