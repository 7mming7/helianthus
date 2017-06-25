package com.ha.flow;

import com.ha.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User: shuiqing
 * DateTime: 17/6/22 下午6:08
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class FlowDto implements Serializable {

    private static final long serialVersionUID = -7816945325851639128L;

    private String flowId;

    private String flowName;

    private String flowDescription;

    private String active;

    private String projectId;

    private String projectName;

    public FlowDto(Flow flow){
        this.flowId = flow.getId().toString();
        this.flowName = flow.getName();
        this.flowDescription = flow.getDescription();
        this.projectId = flow.getProject().getId().toString();
        this.projectName = flow.getProject().getName();
        this.active = flow.getActive() == true ? "是" : "否";
    }
}
