package com.zcf.words.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Setter
@Getter
@Table(name = "panels")
public class Panels {

    @Id
    private Integer panels_id;//主键
    private String panels_title;//展示面标题
    private String panels_pic;//展示面图

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;//创建日期
    private Date update_time;//修改日期
    private String mark;//备注
    private String del_flag;//删除标记 0正常 1已删除

}
