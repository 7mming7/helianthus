package com.ha.graph.project;

import com.ha.entity.AbstractEntity;
import com.ha.graph.flow.Flow;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * import project.
 * User: shuiqing
 * DateTime: 17/6/1 下午4:51
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_Project")
@Getter
@Setter
public class Project extends AbstractEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean active = true;

    @Transient
    private Map<String, Flow> flows = null;

    public void setFlows(Map<String, Flow> flows) {
        this.flows = flows;
    }

    public Flow getFlow(String flowId) {
        if (flows == null) {
            return null;
        }

        return flows.get(flowId);
    }

    public Map<String, Flow> getFlowMap() {
        return flows;
    }

    public List<Flow> getFlows() {
        List<Flow> retFlow = null;
        if (flows != null) {
            retFlow = new ArrayList<Flow>(flows.values());
        } else {
            retFlow = new ArrayList<Flow>();
        }
        return retFlow;
    }
}
