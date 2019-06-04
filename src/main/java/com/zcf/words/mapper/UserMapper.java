package com.zcf.words.mapper;


import com.zcf.words.common.mybatis.MyMapper;
import com.zcf.words.entity.User;
import com.zcf.words.vo.out.UserVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;


public interface UserMapper extends MyMapper<User> {


}
