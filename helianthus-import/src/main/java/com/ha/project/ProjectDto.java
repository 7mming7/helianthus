package com.ha.project;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User: shuiqing
 * DateTime: 17/6/27 上午9:44
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class ProjectDto implements Serializable {

    private String projectId;

    private String name;

    private String description;

    private String active;

    public ProjectDto(Project project){
        this.projectId = project.getId().toString();
        this.name = project.getName();
        this.description = project.getDescription();
        this.active = project.getActive() == true ? "1":"0";
    }
}
