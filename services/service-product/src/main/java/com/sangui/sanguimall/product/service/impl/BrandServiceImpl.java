package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.feign.ThirdPartyFeignClient;
import com.sangui.sanguimall.product.mapper.BrandMapper;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.query.BrandEditShowStatusQuery;
import com.sangui.sanguimall.product.service.BrandService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-07
 * @Description:
 * @Version: 1.0
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Resource
    BrandMapper brandMapper;

    @Resource
    ThirdPartyFeignClient thirdPartyFeignClient;

    @Override
    public PageInfo<BrandDo> getBrandsByPage(Integer current) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<BrandDo> list = brandMapper.selectBrandByPage();

        for (BrandDo brandDo : list) {
            String url = (String)thirdPartyFeignClient.getSignedUrl(brandDo.getLogo()).getData();
            brandDo.setLogo(url);
        }
        // 3. 封装分页数据到 PageInfo
        return new PageInfo<>(list);
    }

    @Override
    public int editBrandShowStatus(BrandEditShowStatusQuery brandEditShowStatusQuery) {
        Long brandId = brandEditShowStatusQuery.getBrandId();
        Boolean flag = brandEditShowStatusQuery.getFlag();
        BrandDo brandDo = brandMapper.selectByPrimaryKey(brandId);
        brandDo.setShowStatus((byte) (flag ? 1 : 0));
        return brandMapper.updateByPrimaryKeySelective(brandDo);
    }
}
