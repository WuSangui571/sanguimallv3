package com.sangui.sanguimall.member.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.member.mapper.UmsGrowthChangeHistoryMapper;
import com.sangui.sanguimall.member.mapper.UmsIntegrationChangeHistoryMapper;
import com.sangui.sanguimall.member.mapper.UmsMemberLevelMapper;
import com.sangui.sanguimall.member.mapper.UmsMemberMapper;
import com.sangui.sanguimall.member.model.entity.UmsGrowthChangeHistory;
import com.sangui.sanguimall.member.model.entity.UmsIntegrationChangeHistory;
import com.sangui.sanguimall.member.model.entity.UmsMember;
import com.sangui.sanguimall.member.model.entity.UmsMemberLevel;
import com.sangui.sanguimall.member.model.query.MemberPageQuery;
import com.sangui.sanguimall.member.model.query.MemberSaveQuery;
import com.sangui.sanguimall.member.model.vo.UmsMemberVo;
import com.sangui.sanguimall.member.service.UmsMemberService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UmsMemberServiceImpl implements UmsMemberService {

    private static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter CREATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UmsMemberMapper memberMapper;
    private final UmsMemberLevelMapper memberLevelMapper;
    private final UmsIntegrationChangeHistoryMapper integrationHistoryMapper;
    private final UmsGrowthChangeHistoryMapper growthHistoryMapper;
    private final PasswordEncoder passwordEncoder;

    public UmsMemberServiceImpl(UmsMemberMapper memberMapper,
            UmsMemberLevelMapper memberLevelMapper,
            UmsIntegrationChangeHistoryMapper integrationHistoryMapper,
            UmsGrowthChangeHistoryMapper growthHistoryMapper,
            PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.memberLevelMapper = memberLevelMapper;
        this.integrationHistoryMapper = integrationHistoryMapper;
        this.growthHistoryMapper = growthHistoryMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageInfo<UmsMemberVo> pageMembers(MemberPageQuery query) {
        int pageNum = query != null && query.getCurrent() != null && query.getCurrent() > 0
                ? query.getCurrent() : 1;
        String keyword = query != null ? trim(query.getKeyword()) : null;
        Long levelId = query != null ? query.getLevelId() : null;
        Byte status = query != null && query.getStatus() != null
                ? query.getStatus().byteValue() : null;

        PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
        List<UmsMember> list = memberMapper.selectByCondition(keyword, levelId, status);
        if (list == null) {
            list = Collections.emptyList();
        }
        PageInfo<UmsMember> pageInfo = new PageInfo<>(list);

        Map<Long, String> levelMap = buildLevelMap();
        List<UmsMemberVo> voList = convertToVoList(list, levelMap);
        PageInfo<UmsMemberVo> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);
        return result;
    }

    @Override
    public UmsMemberVo getById(Long id) {
        if (id == null) {
            return null;
        }
        UmsMember member = memberMapper.selectByPrimaryKey(id);
        if (member == null) {
            return null;
        }
        return toVo(member, buildLevelMap());
    }

    @Override
    public int create(MemberSaveQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        ensureUsernameUnique(query.getUsername(), null);
        UmsMember entity = toEntity(query, true);
        if (!StringUtils.hasText(query.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }
        entity.setPassword(passwordEncoder.encode(query.getPassword()));
        entity.setId(null);
        return memberMapper.insertSelective(entity);
    }

    @Override
    public int update(MemberSaveQuery query) {
        if (query == null || query.getId() == null) {
            throw new IllegalArgumentException("会员 ID 不能为空");
        }
        ensureUsernameUnique(query.getUsername(), query.getId());
        UmsMember exists = memberMapper.selectByPrimaryKey(query.getId());
        if (exists == null) {
            throw new IllegalArgumentException("会员不存在");
        }
        int beforeIntegration = defaultInt(exists.getIntegration());
        int beforeGrowth = defaultInt(exists.getGrowth());
        int afterIntegration = query.getIntegration() != null
                ? query.getIntegration() : beforeIntegration;
        int afterGrowth = query.getGrowth() != null ? query.getGrowth() : beforeGrowth;
        query.setIntegration(afterIntegration);
        query.setGrowth(afterGrowth);
        int integrationChange = afterIntegration - beforeIntegration;
        int growthChange = afterGrowth - beforeGrowth;
        if (growthChange != 0) {
            Long resolvedLevelId = resolveLevelIdByGrowth(afterGrowth);
            if (resolvedLevelId != null) {
                query.setLevelId(resolvedLevelId);
            }
        }
        UmsMember entity = toEntity(query, false);
        if (StringUtils.hasText(query.getPassword())) {
            entity.setPassword(passwordEncoder.encode(query.getPassword()));
        }
        int count = memberMapper.updateByPrimaryKeySelective(entity);
        if (count > 0) {
            if (integrationChange != 0) {
                recordIntegrationChange(exists.getId(), integrationChange, afterIntegration);
            }
            if (growthChange != 0) {
                recordGrowthChange(exists.getId(), growthChange, afterGrowth);
            }
        }
        return count;
    }

    private void ensureUsernameUnique(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        UmsMember existing = memberMapper.selectByUsername(username.trim());
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw new IllegalArgumentException("用户名已存在");
        }
    }

    private Map<Long, String> buildLevelMap() {
        List<UmsMemberLevel> levels = memberLevelMapper.selectAll();
        if (levels == null || levels.isEmpty()) {
            return Collections.emptyMap();
        }
        return levels.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UmsMemberLevel::getId, UmsMemberLevel::getName,
                        (left, right) -> left));
    }

    private List<UmsMemberVo> convertToVoList(List<UmsMember> list, Map<Long, String> levelMap) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(member -> toVo(member, levelMap))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private UmsMemberVo toVo(UmsMember member, Map<Long, String> levelMap) {
        if (member == null) {
            return null;
        }
        UmsMemberVo vo = new UmsMemberVo();
        vo.setId(member.getId());
        vo.setLevelId(member.getLevelId());
        if (levelMap != null) {
            vo.setLevelName(levelMap.get(member.getLevelId()));
        }
        vo.setUsername(member.getUsername());
        vo.setNickname(member.getNickname());
        vo.setMobile(member.getMobile());
        vo.setEmail(member.getEmail());
        vo.setHeader(member.getHeader());
        vo.setGender(member.getGender());
        vo.setBirth(member.getBirth());
        vo.setCity(member.getCity());
        vo.setJob(member.getJob());
        vo.setSign(member.getSign());
        vo.setSourceType(member.getSourceType());
        vo.setIntegration(member.getIntegration());
        vo.setGrowth(member.getGrowth());
        vo.setStatus(member.getStatus());
        vo.setCreateTime(member.getCreateTime());
        vo.setCreateTimeText(formatCreateTime(member.getCreateTime()));
        return vo;
    }

    private UmsMember toEntity(MemberSaveQuery query, boolean isCreate) {
        UmsMember entity = new UmsMember();
        entity.setId(query.getId());
        entity.setLevelId(query.getLevelId());
        entity.setUsername(trim(query.getUsername()));
        entity.setNickname(query.getNickname());
        entity.setMobile(query.getMobile());
        entity.setEmail(query.getEmail());
        entity.setHeader(query.getHeader());
        entity.setGender(toByte(query.getGender()));
        entity.setBirth(toDate(query.getBirth()));
        entity.setCity(query.getCity());
        entity.setJob(query.getJob());
        entity.setSign(query.getSign());
        entity.setSourceType(toByteOrDefault(query.getSourceType(), (byte) 0));
        entity.setIntegration(resolveNumericValue(query.getIntegration(), isCreate));
        entity.setGrowth(resolveNumericValue(query.getGrowth(), isCreate));
        entity.setStatus(toByteOrDefault(query.getStatus(), (byte) 1));
        entity.setCreateTime(isCreate ? new Date() : null);
        return entity;
    }

    private Integer resolveNumericValue(Integer value, boolean isCreate) {
        if (value != null) {
            return value;
        }
        return isCreate ? 0 : null;
    }

    private Byte toByte(Integer value) {
        return value == null ? null : value.byteValue();
    }

    private Byte toByteOrDefault(Integer value, byte defaultValue) {
        return value == null ? defaultValue : value.byteValue();
    }

    private Integer defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String formatCreateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZONE_SHANGHAI);
        return CREATE_TIME_FORMATTER.format(zonedDateTime);
    }

    private void recordIntegrationChange(Long memberId, int changeValue, int updatedValue) {
        if (memberId == null || changeValue == 0) {
            return;
        }
        UmsIntegrationChangeHistory history = new UmsIntegrationChangeHistory();
        history.setMemberId(memberId);
        history.setCreateTime(new Date());
        history.setChangeCount(changeValue);
        history.setNote(buildIntegrationNote(changeValue, updatedValue));
        history.setSourceTyoe((byte) 1);
        integrationHistoryMapper.insertSelective(history);
    }

    private void recordGrowthChange(Long memberId, int changeValue, int updatedValue) {
        if (memberId == null || changeValue == 0) {
            return;
        }
        UmsGrowthChangeHistory history = new UmsGrowthChangeHistory();
        history.setMemberId(memberId);
        history.setCreateTime(new Date());
        history.setChangeCount(changeValue);
        history.setNote(buildGrowthNote(changeValue, updatedValue));
        history.setSourceType((byte) 1);
        growthHistoryMapper.insertSelective(history);
    }

    private String buildIntegrationNote(int changeValue, int updatedValue) {
        String action = changeValue > 0 ? "增加" : "减去";
        return String.format("管理员手动%s「%d」积分，积分值更新为「%d」。",
                action, Math.abs(changeValue), updatedValue);
    }

    private String buildGrowthNote(int changeValue, int updatedValue) {
        String action = changeValue > 0 ? "增加" : "减去";
        return String.format("管理员手动%s「%d」成长值，成长值更新为「%d」。",
                action, Math.abs(changeValue), updatedValue);
    }

    private Long resolveLevelIdByGrowth(Integer growthValue) {
        List<UmsMemberLevel> levels = memberLevelMapper.selectAll();
        if (levels == null || levels.isEmpty()) {
            return null;
        }
        Long defaultLevelId = resolveDefaultLevelId(levels);
        if (growthValue == null || growthValue < 0) {
            return defaultLevelId;
        }
        UmsMemberLevel matched = null;
        for (UmsMemberLevel level : levels) {
            if (level == null || level.getGrowthPoint() == null || level.getId() == null) {
                continue;
            }
            if (growthValue >= level.getGrowthPoint()) {
                if (matched == null || level.getGrowthPoint() > matched.getGrowthPoint()) {
                    matched = level;
                }
            }
        }
        if (matched != null && matched.getId() != null) {
            return matched.getId();
        }
        return defaultLevelId;
    }

    private Long resolveDefaultLevelId(List<UmsMemberLevel> levels) {
        if (levels == null) {
            return 1L;
        }
        for (UmsMemberLevel level : levels) {
            if (level != null && level.getDefaultStatus() != null
                    && level.getDefaultStatus() == 1 && level.getId() != null) {
                return level.getId();
            }
        }
        for (UmsMemberLevel level : levels) {
            if (level != null && level.getId() != null) {
                return level.getId();
            }
        }
        return 1L;
    }
}
