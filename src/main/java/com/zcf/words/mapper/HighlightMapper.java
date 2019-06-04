package com.zcf.words.mapper;


import com.zcf.words.common.mybatis.MyMapper;
import com.zcf.words.entity.Highlight;
import com.zcf.words.entity.User;
import com.zcf.words.vo.out.HighLightContent;

import java.util.List;
import org.springframework.stereotype.Repository;

public interface HighlightMapper extends MyMapper<Highlight> {


    public List<Highlight> findHighlight(String toptagid);
}
