package cn.dust.server.service.impl;

import cn.dust.common.response.StatusCode;
import cn.dust.common.utils.CommonUtil;
import cn.dust.common.utils.PageUtil;
import cn.dust.common.utils.QueryUtil;
import cn.dust.model.entity.SysPost;
import cn.dust.model.mapper.SysPostMapper;
import cn.dust.server.service.SysPostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: dust
 * @Date: 2019/10/13 15:29
 */
@Service("sysPostService")
@Slf4j
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService{

    /**
     * 分页模糊查询
     * @param params
     */
    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        String search=(String)params.get("search")==null?"":params.get("search").toString();
//        IPage<SysPost>queryPage=new Page<SysPost>();
//        queryPage.setCurrent(Long.valueOf(currPage));
        //调用自封装的分页查询工具
         IPage<SysPost> queryPage=new QueryUtil<SysPost>().getQueryPage(params);

        QueryWrapper wrapper=new QueryWrapper<SysPostMapper>()
                .like(!StringUtils.isEmpty(search),"post_code",search)
                .or(!StringUtils.isEmpty(search))
                .like(!StringUtils.isEmpty(search),"post_name",search);

        IPage<SysPost>resPage=this.page(queryPage,wrapper);

        log.info("查询结果{}",resPage);

        return new PageUtil(resPage);
    }

    //新增
    @Override
    public void savePost(SysPost sysPost) {
        //校验postcode是否唯一
        if(this.getOne(new QueryWrapper<SysPost>().eq("post_code",sysPost.getPostCode()))!=null){
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        sysPost.setCreateTime(DateTime.now().toDate());
        save(sysPost);
    }

    //修改岗位
    @Override
    public void updatePost(SysPost sysPost) {

        SysPost old=this.getById(sysPost.getPostId());
        if (old!=null && !old.getPostCode().equals(sysPost.getPostCode())){
            if (this.getOne(new QueryWrapper<SysPost>().eq("post_code",sysPost.getPostCode()))!=null){
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }
        sysPost.setUpdateTime(DateTime.now().toDate());
        updateById(sysPost);
    }

    //批量删除
    @Override
    public void deletePatch(Long[] ids) {
//        //第一种写法 - mybatis-plus
//        removeByIds(Arrays.asList(ids));

//        第二种写法
//        ids=[1,2,3,4,5];  ->  "1,2,3,4,5"
        String delIds=Joiner.on(",").join(ids);
        baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
    }
}
