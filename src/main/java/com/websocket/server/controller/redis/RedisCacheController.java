package com.websocket.server.controller.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.server.model.RedisCache;
import com.websocket.server.service.RedisCacheService;


@RestController
@RequestMapping("redis-keys")
public class RedisCacheController {

	@Autowired
	private RedisCacheService redisCacheService;
	
	/**
	 * @Cacheable 어노테이션을 Service 혹은 Controller에 추가하여 캐싱 사용을 등록할 수 있지만, 
	 * 여기서는 아래와 같이 Controller에 추가합니다. 
	 * value = "RedisCash" : 저장될 value로 API의 리턴 데이터인 RedisCash 객체로 선언
	 * key = "#id" : 이 API에서 id에 따라 응답값이 달라지므로 저장될 Key로 id 파라미터 값을 선언 
	 * cacheManager = "cacheManager" : 위의 config에서 작성한 cacheManager 사용 (생략가능)
	 * unless = "#id == ''" : id가 "" 일때 캐시를 저장하지 않음
	 * condition = "#id.length > 2" : id의 lengrh가 3 이상일 때만 캐시 저장
	 */
    @GetMapping
    @Cacheable(value = "getData", key = "#id", cacheManager = "cacheManager", unless = "#id == ''", condition = "#id.length > 2")
    public RedisCache getData(@RequestParam String id ){
        return redisCacheService.getRedisCash(id);
    }
    
	@Cacheable(cacheNames = "cash")
	public void getCache() throws Exception {
	}
	
	@CacheEvict(cacheNames = "refresh", allEntries=true)
	public void getRefresh() throws Exception {
	}
}
