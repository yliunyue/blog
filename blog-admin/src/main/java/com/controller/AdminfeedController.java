package com.controller;

import com.service.AdminfeedService;
import com.vo.Result;
import com.vo.params.AdminFeedParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-04-13
 */
@RestController
@RequestMapping("/adminfeed")
public class AdminfeedController {
    @Autowired
    private AdminfeedService adminfeedService;
    @PostMapping
    public Result adminFeed(@RequestBody AdminFeedParam adminFeedParam){
        return adminfeedService.adminFeed(adminFeedParam);

    }


}
