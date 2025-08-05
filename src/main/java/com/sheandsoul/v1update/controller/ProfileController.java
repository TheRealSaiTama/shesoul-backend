package com.sheandsoul.v1update.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sheandsoul.v1update.dto.ProfilePatchRequest;
import com.sheandsoul.v1update.services.AppService;
import com.sheandsoul.v1update.services.MyUserDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final AppService appService;
    private final MyUserDetailService userDetailService;

    public ProfileController(AppService appService, MyUserDetailService userDetailService) {
        this.appService = appService;
        this.userDetailService = userDetailService;
    }

    @PatchMapping("/basic")
    public ResponseEntity<?> patchBasicInfo(@Valid @RequestBody ProfilePatchRequest patch,
                                            Authentication authentication) {
        var currentUser = userDetailService.findUserByEmail(authentication.getName());
        appService.patchProfileBasic(currentUser.getId(), patch);
        return ResponseEntity.ok().build();
    }
}
