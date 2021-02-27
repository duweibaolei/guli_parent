package com.dwl.service_edu.controller.front;

import com.dwl.common_utils.Result.Result;
import com.dwl.common_utils.ordervo.CourseWebVoOrder;
import com.dwl.common_utils.util.JwtUtil;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_edu.client.OrdersClient;
import com.dwl.service_edu.entity.vo.ChapterNestedVo;
import com.dwl.service_edu.entity.vo.frontvo.CourseWebVo;
import com.dwl.service_edu.entity.vo.frontvo.FrontCourseQueryVo;
import com.dwl.service_edu.service.EduChapterService;
import com.dwl.service_edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "前端课程管理")
@RequestMapping("/eduService/front/courseFront")
@RestController
@CrossOrigin
public class CourseFrontController {

    /**
     * 课程基本信息服务类
     */
    private EduCourseService eduCourseService;

    /**
     * 课程 服务类
     */
    private EduChapterService chapterService;

    private OrdersClient ordersClient;

    public CourseFrontController() {
    }

    @Autowired
    public CourseFrontController(EduCourseService eduCourseService, EduChapterService chapterService, OrdersClient ordersClient) {
        this.eduCourseService = eduCourseService;
        this.chapterService = chapterService;
        this.ordersClient = ordersClient;
    }

    /**
     * @param current 当前页
     * @param limit   每页记录数
     * @param queryVo 前端课程查询实体类
     * @return Result
     */
    @ApiOperation(value = "分页课程列表")
    @PostMapping("/pageCourse/{current}/{limit}")
    public Result pageListTeacher(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit,
            @ApiParam(name = "queryVo", value = "前端课程查询实体类") @RequestBody FrontCourseQueryVo queryVo) {
        Map<String, Object> map = eduCourseService.pageListWeb(current, limit, queryVo);
        return Result.ok().data(map);
    }

    /**
     * 课程详情的方法
     *
     * @param courseId 课程id
     * @return Result
     */
    @ApiOperation(value = "课程详情的方法")
    @GetMapping("getFrontCourseInfo/{courseId}")
    public Result getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        // 根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(courseId);
        // 根据课程id查询章节和小节
        List<ChapterNestedVo> chapterVideoList = chapterService.nestedChapterList(courseId);
        // 根据课程id和用户id查询当前课程是否已经支付过了
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        boolean buyCourse = false;
        if (StringUtil.isNotEmpty(memberId)) {
            buyCourse = ordersClient.isBuyCourse(courseId, JwtUtil.getMemberIdByJwtToken(request));
        }
        return Result.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList).data("isBuy", buyCourse);
    }

    /**
     * 根据课程id查询课程信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据课程id查询课程信息")
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id) {
        CourseWebVo courseInfo = eduCourseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo, courseWebVoOrder);
        return courseWebVoOrder;
    }
}
