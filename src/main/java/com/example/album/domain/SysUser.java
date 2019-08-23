package com.example.album.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.joda.time.DateTime;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements SaltedAuthenticationInfo {
    private String userId;
    private String userName;
    private String phone;
    private String email;
    private String password;
    private String salt;
    private String locked;
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
}













