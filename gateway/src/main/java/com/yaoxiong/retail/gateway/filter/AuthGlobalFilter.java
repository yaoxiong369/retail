package com.yaoxiong.retail.gateway.filter;

import com.alibaba.fastjson.JSONObject;

import com.yaoxiong.retail.gateway.util.HttpUtil;
import com.yaoxiong.retail.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    com.yaoxiong.retail.redis.utils.RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        exchange.getSession().flatMap(webSession -> {
//            System.out.println("-----------------"+(String)webSession.getAttribute("token"));
//            return chain.filter(exchange);
//        });
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(antPathMatcher.match("/*/test/**", path)) {
            return chain.filter(exchange);
        }


        System.out.println("==="+path);
        String id = HttpUtil.getSessionId(request);
        System.out.println("id = " + id);
        try{

            final String jwt  = redisUtil.getHashValues("spring:session:sessions:"+HttpUtil.getSessionId(request),"sessionAttr:token").toString();

            System.out.println( "jwt = "+ jwt );
            if(jwt != null){
//            exchange.getAttributes().put("token",jwt);
                exchange.getRequest().mutate().headers(h->h.add("token",jwt)).build();
//            request.getHeaders().add("token" , jwt);
            }

        }catch (Exception e){
            System.out.println("e = " + e.getMessage() );
        }


//        //??????????????????????????????????????????
//        if(antPathMatcher.match("/**/inner/**", path)) {
//            ServerHttpResponse response = exchange.getResponse();
//            return out(response, ResultCodeEnum.PERMISSION);
//        }
//
//        //api????????????????????????????????????????????????
//        if(antPathMatcher.match("/api/**/auth/**", path)) {
//            Long userId = this.getUserId(request);
//            if(StringUtils.isEmpty(userId)) {
//                ServerHttpResponse response = exchange.getResponse();
//                return out(response, ResultCodeEnum.LOGIN_AUTH);
//            }
//        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * api??????????????????????????????
     * @param response
     * @return
     */
//    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
//        Result result = Result.build(null, resultCodeEnum);
//        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = response.bufferFactory().wrap(bits);
//        //???????????????????????????????????????????????????
//        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//        return response.writeWith(Mono.just(buffer));
//    }

    /**
     * ????????????????????????id
     * @param request
     * @return
     */
//    private Long getUserId(ServerHttpRequest request) {
//        String token = "";
//        List<String> tokenList = request.getHeaders().get("token");
//        if(null  != tokenList) {
//            token = tokenList.get(0);
//        }
//        if(!StringUtils.isEmpty(token)) {
//            return JwtHelper.getUserId(token);
//        }
//        return null;
//    }
}
