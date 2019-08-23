package com.example.album.dao;

import com.example.album.domain.SysUser;
import org.n3r.eql.eqler.annotations.EqlerConfig;
import org.n3r.eql.eqler.annotations.Sql;
import org.springframework.stereotype.Repository;

import java.util.List;

@EqlerConfig
@Repository
public interface UserDao {

    @Sql("select * from sys_user")
    List<SysUser> getAllUsers();

    @Sql("select * from sys_user where user_id = #_1#")
    SysUser getUserById(String userId);

    @Sql("select * from sys_user where user_name=#_1# or phone=#_1# or email=#_1#")
    SysUser getUser(String principal);

    @Sql("insert into sys_user(user_id,user_name,phone,email,password,salt)\n" +
            "values(#userId#,#userName#,#phone#,#email#,#password#,#salt#)")
    int createUser(SysUser user);

    @Sql("update sys_user\n" +
            "set \n" +
            "\tuser_name=#userName#,\n" +
            "\tphone=#phone#,\n" +
            "\temail=#email#,\n" +
            "\tpassword=#password#,\n" +
            "\tsalt=#salt#,\n" +
            "\tupdate_time=#updateTime#\n" +
            "WHERE\n" +
            "\tuser_id=#userId#")
    int updateUser(SysUser user);

    @Sql("delete from sys_user where user_id = ##")
    int deleteUser(String userId);
}
