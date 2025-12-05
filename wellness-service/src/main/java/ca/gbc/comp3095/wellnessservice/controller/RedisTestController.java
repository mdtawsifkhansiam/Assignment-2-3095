package ca.gbc.comp3095.wellnessservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;

@RestController
@RequestMapping("/redis-test")
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<String> testRedis() {
        try {
            redisTemplate.opsForValue().set("ping", "pong");
            String value = redisTemplate.opsForValue().get("ping");
            return ResponseEntity.ok("Redis is working: " + value);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Redis error: " + e.getMessage());
        }
    }
}