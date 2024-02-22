package vn.com.gsoft.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.security.service.UserCacheService;

@RestController
@RequestMapping("/caches")
public class CacheController {
    @Autowired
    private UserCacheService userCacheService;

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<Boolean> clearCacheByUsername(@RequestParam("username") String username) {
        userCacheService.clearCacheByUsername(username);
        return ResponseEntity.ok(true);
    }

}
