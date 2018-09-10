package top.vetoer.domain;

import javax.persistence.*;

@Entity
public class Timeline extends Entitys{

    private static final long serialVersionUID = 1672593873891396164L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable=false)
    private long userId;
    @Column(nullable = false,length = 655350,columnDefinition = "Text")
    private String content;
    @Column(nullable = false)
    private String isMd;
    @Column(nullable = true)
    private Long createTime;

    public Timeline() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsMd() {
        return isMd;
    }

    public void setIsMd(String isMd) {
        this.isMd = isMd;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
