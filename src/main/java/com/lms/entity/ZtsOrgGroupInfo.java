package com.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZtsOrgGroupInfo {

    /**
     * 组id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;

    /**
     * 组路径
     */
    private String groupNamePath;

    /**
     * 上级id
     */
    private Long groupPid;

    /**
     * 描述
     */
    private String description;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 所属域
     */
    private String domainId;

    /**
     * 更新时间
     */
    private String updateTime;

    private String extraId;

    private String extraParentId;

    /**
     * 子组数量
     */
    @TableField(exist = false)
    private Integer childCount;

    /**
     * 当前组用户数量
     */
    private Integer userCount;

    private static final long serialVersionUID = 1L;
}
