package com.dwl.service_edu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_edu.client.UcenterClient;
import com.dwl.service_edu.entity.EduComment;
import com.dwl.service_edu.entity.uc.UcenterMember;
import com.dwl.service_edu.service.EduCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/front/commentFront")
@CrossOrigin
public class CommentFrontController {

    /**
     * 评论 服务类
     */
    private EduCommentService commentService;

    /**
     * 远程调用登录类
     */
    private UcenterClient ucenterClient;

    @Autowired
    public CommentFrontController(EduCommentService commentService, UcenterClient ucenterClient) {
        this.commentService = commentService;
        this.ucenterClient = ucenterClient;
    }

    public CommentFrontController() {
    }


    /**
     * 根据课程id查询评论列表
     *
     * @param page
     * @param limit
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据课程id查询评论列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象")
                    String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        commentService.page(pageParam, wrapper);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return Result.ok().data(map);
    }

    /**
     * 添加评论
     *
     * @param comment
     * @param request
     * @return
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public Result save(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        if (StringUtil.isEmpty(memberId)) {
            return Result.error().code(ResultCode.ERROR.getStatus()).message("请登录");
        }
        comment.setMemberId(memberId);
        UcenterMember ucenterInfo = ucenterClient.getUcenterPay(memberId);
        comment.setNickname(ucenterInfo.getNickname());
        comment.setAvatar(ucenterInfo.getAvatar());
        commentService.save(comment);
        return Result.ok();
    }
}
