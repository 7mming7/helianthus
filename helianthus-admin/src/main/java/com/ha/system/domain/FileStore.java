package com.ha.system.domain;

import com.ha.base.domain.BaseEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 文件存储
 * User: shuiqing
 * DateTime: 17/4/25 下午1:44
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_FileStore")
public class FileStore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "fileType", nullable = false)
    private int fileType = FileType.IMAGE.getValue();

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name=" content", columnDefinition = "mediumtext", nullable = true)
    private byte[] content;

    private Calendar uploadTime;

    private String cid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Calendar getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Calendar uploadTime) {
        this.uploadTime = uploadTime;
    }
}