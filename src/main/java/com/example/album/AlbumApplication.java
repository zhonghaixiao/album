package com.example.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.bingoohuang.utils.redis.JedisProxy;
import lombok.val;
import org.n3r.eql.eqler.spring.EqlerScan;
import org.n3r.eql.trans.spring.annotation.EnableEqlTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@EnableEqlTransaction
@EqlerScan
@SpringBootApplication
@Configuration
public class AlbumApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlbumApplication.class, args);
	}

	@Bean
	public JedisPool createJedisPool() {
		String host = "127.0.0.1";
		int port =  6379;
		int timeout = 2000;
		int database = 0;
		int maxClients = 50;

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxClients);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setMinIdle(8);

		return new JedisPool(jedisPoolConfig, host, port, timeout, null, database);
	}

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

}
