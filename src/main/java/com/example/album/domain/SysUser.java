package com.example.album.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements SaltedAuthenticationInfo, AuthorizationInfo {
    private String userId;
    private String userName;
    private String phone;
    private String email;
    private String password;
    private String salt;
    private String locked;
    private List<Role> roles;
    private List<Perm> perms;
    private DateTime createTime;
    private DateTime updateTime;

    @Override
    public ByteSource getCredentialsSalt() {
        return ByteSource.Util.bytes(salt);
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(Arrays.asList(userName, phone, email), "myRealm");
    }

    @Override
    public char[] getCredentials() {
        return password.toCharArray();
    }

    @Override
    public Collection<String> getRoles() {
        if (roles == null) {
            return new ArrayList<>();
        }
        return roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    @Override
    public Collection<String> getStringPermissions() {
        if (perms == null){
            return new ArrayList<>();
        }
        return perms.stream().map(Perm::getPermName).collect(Collectors.toSet());
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return perms.stream().map(p-> new WildcardPermission(p.getPermName())).collect(Collectors.toSet());
    }
}













