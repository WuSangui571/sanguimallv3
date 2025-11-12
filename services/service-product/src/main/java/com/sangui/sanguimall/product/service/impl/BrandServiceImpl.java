package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.feign.ThirdPartyFeignClient;
import com.sangui.sanguimall.product.mapper.BrandMapper;
import com.sangui.sanguimall.product.model.converter.BrandConverter;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.query.AddBrandQuery;
import com.sangui.sanguimall.product.model.query.BrandEditShowStatusQuery;
import com.sangui.sanguimall.product.model.vo.SingleBrandVo;
import com.sangui.sanguimall.product.service.BrandService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    BrandConverter brandConverter;

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

    @Override
    public int addBrand(AddBrandQuery addBrandQuery) {
        System.out.println("后端收到的前端信息：" + addBrandQuery);
        BrandDo brandDo = new BrandDo();
        brandDo.setName(addBrandQuery.getName());
        brandDo.setLogo(addBrandQuery.getLogo());
        brandDo.setDescript(addBrandQuery.getDescript());
        brandDo.setShowStatus(addBrandQuery.getShowStatus());
        brandDo.setFirstLetter(addBrandQuery.getFirstLetter());
        brandDo.setSort(addBrandQuery.getSort());
        return brandMapper.insert(brandDo);
    }

    @Override
    public SingleBrandVo getSingeBrandById(Long id) {
        BrandDo brandDo = brandMapper.selectByPrimaryKey(id);
        return brandConverter.doToVo(brandDo);
    }

    @Override
    public int editBrand(AddBrandQuery addBrandQuery) {
        BrandDo brandDo = new BrandDo();
        brandDo.setBrandId(addBrandQuery.getId());
        brandDo.setName(addBrandQuery.getName());
        brandDo.setLogo(addBrandQuery.getLogo());
        brandDo.setDescript(addBrandQuery.getDescript());
        brandDo.setShowStatus(addBrandQuery.getShowStatus());
        brandDo.setFirstLetter(addBrandQuery.getFirstLetter());
        brandDo.setSort(addBrandQuery.getSort());
        return brandMapper.updateByPrimaryKeySelective(brandDo);
    }

    @Override
    public int delBrandById(Long id) {
        BrandDo brandDo = brandMapper.selectByPrimaryKey(id);
        String message = (String) thirdPartyFeignClient.deleteFile(brandDo.getLogo()).getData();
        System.out.println("单次删除的结果：" + message);
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delBrandByIds(String ids) {
        List<BrandDo> brandList = brandMapper.selectBrandByIds(ids);
        List<String> logoList = new ArrayList<>();
        for (BrandDo brandDo: brandList){
            logoList.add(brandDo.getLogo());
        }
        String message = (String) thirdPartyFeignClient.deleteFiles(logoList).getData();
        System.out.println("批量删除的结果：" + message);
        return brandMapper.deleteByIds(ids);
    }

    @Override
    public PageInfo<BrandDo> getBrandsBySelect(Integer current, String selectValue) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<BrandDo> list = brandMapper.selectBrandBySelect(selectValue);

        for (BrandDo brandDo : list) {
            String url = (String)thirdPartyFeignClient.getSignedUrl(brandDo.getLogo()).getData();
            brandDo.setLogo(url);
        }
        // 3. 封装分页数据到 PageInfo
        return new PageInfo<>(list);
    }
}
