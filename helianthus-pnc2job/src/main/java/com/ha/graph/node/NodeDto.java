package com.ha.graph.node;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/6/26 下午2:31
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class NodeDto implements Serializable {

    private String nodeId;

    private String nodeName;

    private String nodeDesc;

    private String active;

    private String jobType;

    private String projectId;

    private String projectName;

    private String flowId;

    private String flowName;

    private String level;

    private String dependsNode;

    private String params;

    private String executeStatus;

    /*private Map<String,String> paramsMap = new HashMap<String,String>();*/

    public NodeDto(Node node){
        this.nodeId = node.getId().toString();
        this.nodeName = node.getName();
        this.nodeDesc = node.getDescription();
        this.active = node.getActive() == true ? "1" : "0";
        this.jobType = node.getJobType();
        this.dependsNode = node.getDepends();
        this.level = node.getLevel() == null ? "":node.getLevel().toString();
        this.projectId = node.getProject() == null ? "":node.getProject().getId().toString();
        this.projectName = node.getProject() == null ? "":node.getProject().getName();
        this.flowId = node.getFlow() == null ? "":node.getFlow().getId().toString();
        this.flowName = node.getFlow() == null ? "":node.getFlow().getName();
        this.params = node.getParams() == null ? "":node.getParams();
        this.executeStatus = node.getExecuteStatus() == null ? "":node.getExecuteStatus().toString();

        //node params
        /*if(null != node.getParams()){
            String[] paramsArray = node.getParams().split(";");
            for(String param:paramsArray){
                String[] pa = param.split(",");
                paramsMap.put(pa[0],pa[1]);
            }
        }*/
    }
}
