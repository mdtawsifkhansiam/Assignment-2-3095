package ca.gbc.comp3095.wellnessservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class WellnessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WellnessServiceApplication.class, args);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(
			@Value("${spring.data.redis.host}") String host,
			@Value("${spring.data.redis.port}") int port) {

		LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(10))
				.disableCachingNullValues()
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
				);

		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(config)
				.build();
	}
}