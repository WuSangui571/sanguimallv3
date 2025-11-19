package com.sangui.sanguimall.member.mapper;

import com.sangui.sanguimall.member.model.entity.UmsMember;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsMember record);

    int insertSelective(UmsMember record);

    UmsMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsMember record);

    int updateByPrimaryKey(UmsMember record);

    List<UmsMember> selectByCondition(@Param("keyword") String keyword,
            @Param("levelId") Long levelId,
            @Param("status") Byte status);

    UmsMember selectByUsername(String username);
}
