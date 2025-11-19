package com.sangui.sanguimall.member.mapper;

import com.sangui.sanguimall.member.model.entity.UmsIntegrationChangeHistory;
import com.sangui.sanguimall.member.model.vo.IntegrationChangeHistoryVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsIntegrationChangeHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsIntegrationChangeHistory record);

    int insertSelective(UmsIntegrationChangeHistory record);

    UmsIntegrationChangeHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsIntegrationChangeHistory record);

    int updateByPrimaryKey(UmsIntegrationChangeHistory record);

    List<IntegrationChangeHistoryVo> selectWithMember(@Param("keyword") String keyword);
}
