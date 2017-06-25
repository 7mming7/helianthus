package com.ha.flow;

import com.ha.entity.AbstractEntity;
import com.ha.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 调度图对象
 * User: shuiqing
 * DateTime: 17/6/1 下午4:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_Flow")
@Getter
@Setter
public class Flow extends AbstractEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinColumn(name="projectId")
    @ForeignKey(name="fk_pf_projectId")
    @NotFound(action= NotFoundAction.IGNORE)
    private Project project;

    private String description;

    private Boolean active = true;
}
