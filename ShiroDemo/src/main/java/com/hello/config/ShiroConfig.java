package com.hello.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    //配置凭证匹配器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");//散列算法:MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512等。
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，默认1次， 设置两次相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }



    /**
     * 配置session监听
     * @return
     */
  /*  @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }*/

    //3创建shiroFilterfactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean (DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean sff = new ShiroFilterFactoryBean();
        //设置安全认证管理器
        sff.setSecurityManager(securityManager);

        /**
         * shiro内置过滤器
         *  常用过滤器
         *      anon：放行
         *      authc：必须认证
         *      user：如果使用rememberMe功能可以访问
         *      perms：获得资源权限才能访问
         *      role：获得角色权限才能访问
         */
        Map<String,String> filterMap=new LinkedHashMap<>();

        sff.setUnauthorizedUrl("/noAuth");

        //1,放行（过滤器有一定顺序）
        filterMap.put("/hello","anon");
        filterMap.put("/tologin","anon");
        filterMap.put("/login","anon");

        //2，授权
        filterMap.put("/del","perms[user:del]");

        //3,认证
        filterMap.put("/*","authc");

        //4，设置页面
        sff.setLoginUrl("/tologin");


        sff.setFilterChainDefinitionMap(filterMap);

        return sff;
    }

    //2创建defaultWebSecurityManager
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){

        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(userRealm);
        return dwsm;
    }

    //1创建Realm
    @Bean("userRealm")
    public UserRealm getUserBean(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher hashedCredentialsMatcher){
        UserRealm userRealm = new UserRealm();
        userRealm.setAuthorizationCachingEnabled(true);
        //注册身份验证
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return userRealm;
    }


    @Bean("shiroDialect")//开启shiro+thymeleaf标签整合使用
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }


    /**
     * 开启aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
}
