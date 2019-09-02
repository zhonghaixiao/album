package com.example.album.dao;

import com.example.album.domain.Perm;
import com.example.album.domain.Role;
import com.example.album.domain.SysUser;
import com.example.album.domain.SysUser2;
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

    @Sql("select * from sys_user where user_name=#_1# or phone=#_1# or email=#_1#")
    SysUser2 getUser2(String principal);

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

    @Sql("select \n" +
            "\tur.role_id,\n" +
            "\tr.role_name,\n" +
            "\tr.role_desc,\n" +
            "\tr.available\n" +
            "FROM\n" +
            "\tuser_role ur left join role r on ur.role_id=r.role_id\n" +
            "where\n" +
            "\tur.user_id=##")
    List<Role> getAllRoles(String userId);

    @Sql("select\n" +
            "\tp.perm_id,\n" +
            "\tp.perm_name,\n" +
            "\tp.perm_desc\n" +
            "FROM\n" +
            "\tuser_role ur left join role_perm rp on ur.role_id=rp.role_id\n" +
            "\tinner join perm p on rp.perm_id=p.perm_id\n" +
            "WHERE\n" +
            "\tur.user_id=##")
    List<Perm> getAllPerms(String userId);

    @Sql("insert into user_role(user_id,role_id)\n" +
            "values \n" +
            "-- for item=item index=index collection=_2 open='' separator=, close=''\n" +
            "(#_1#, #item#)\n" +
            "-- end")
    int insertRole(String userId, List<String> collect);
}
