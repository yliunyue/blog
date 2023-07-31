package com.controller;

import com.entity.Tag;
import com.service.TagService;
import com.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2023-01-20
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    /*
    * 标签列表,没必要分页了
    * */
    @GetMapping("/tagList")
    private Result tagList(@RequestParam(value = "tagName",required = false) String tagName){
        return tagService.tagList(tagName);
    }

    /*
    * 添加标签
    * */
    @PostMapping("/addTag")
    private Result addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }
    /*
    * 修改标签
    * */
    @PutMapping("/uploadTag")
    private Result uploadTag(@RequestBody Tag tag){
        return tagService.uploadTag(tag);
    }

    /*
    * 删除，根据id
    * */
    @DeleteMapping("/deleteTag/{id}")
    private Result deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }
}
