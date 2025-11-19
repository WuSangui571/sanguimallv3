package com.sangui.sanguimall.member.mapper;

import com.sangui.sanguimall.member.model.entity.UmsGrowthChangeHistory;
import com.sangui.sanguimall.member.model.vo.GrowthChangeHistoryVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsGrowthChangeHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsGrowthChangeHistory record);

    int insertSelective(UmsGrowthChangeHistory record);

    UmsGrowthChangeHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsGrowthChangeHistory record);

    int updateByPrimaryKey(UmsGrowthChangeHistory record);

    List<GrowthChangeHistoryVo> selectWithMember(@Param("keyword") String keyword);
}
