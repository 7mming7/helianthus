package com.ha.system.dto;

import com.ha.system.domain.Dictionary;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User: shuiqing
 * DateTime: 17/8/10 下午3:02
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class DictionaryDto implements Serializable {

    private String dicId;

    private String attribute;

    private String tableName;

    private String value;

    private String description;

    private String status;

    private String serialno;

    public DictionaryDto(Dictionary dictionary){
        this.dicId = dictionary.getId().toString();
        this.attribute = dictionary.getAttribute();
        this.tableName = dictionary.getTableName();
        this.value = dictionary.getValue().toString();
        this.description = dictionary.getDescription();
        this.status = dictionary.getStatus().toString();
        this.serialno = dictionary.getSerialno().toString();
    }
}
