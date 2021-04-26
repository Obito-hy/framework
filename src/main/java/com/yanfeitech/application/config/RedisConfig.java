package com.yanfeitech.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;

/**
 * 
 * <p>
 * Title: RedisConfig
 * </p>
 * <p>
 * Description: redis配置类，使用fastjson进行序列化与反序列化
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Configuration
public class RedisConfig {
	@Bean
	public RedisSerializer<Object> fastJson2JsonRedisSerializer() {
		return new FastJson2JsonRedisSerializer<Object>(Object.class);
	}

	@Bean
	public RedisTemplate<?, ?> initRedisTemplate(RedisConnectionFactory redisConnectionFactory,
			RedisSerializer<?> fastJson2JsonRedisSerializer) throws Exception {
		ParserConfig.getGlobalInstance().addAccept("com.yanfeitech.application.entity");
		ParserConfig.getGlobalInstance().addAccept("com.yanfeitech.application.vo");
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
