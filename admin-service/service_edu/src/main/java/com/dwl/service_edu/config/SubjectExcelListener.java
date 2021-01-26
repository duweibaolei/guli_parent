package com.dwl.service_edu.config;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dwl.common_utils.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.entity.EduSubject;
import com.dwl.service_edu.entity.excel.ExcelSubjectData;
import com.dwl.service_edu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService subjectService;

    @Autowired
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * 一行一行去读取excle内容
     *
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if (excelSubjectData == null) {
            throw new GuLiException(20001, "添加失败");
        }

        if(StringUtil.isEmpty(excelSubjectData.getOneSubjectName())){
            return;
        }
        //添加一级分类
        EduSubject existOneSubject = this.existOneSubject(subjectService, excelSubjectData.getOneSubjectName());
        if (existOneSubject == null) {//没有相同的
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            subjectService.save(existOneSubject);
        }
        if(StringUtil.isEmpty(excelSubjectData.getTwoSubjectName())){
            return;
        }
        //获取一级分类id值
        String pid = existOneSubject.getId();
        //添加二级分类
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, excelSubjectData.getTwoSubjectName(), pid);
        if (existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            subjectService.save(existTwoSubject);
        }
    }

    /**
     * 判断一级分类是否重复
     *
     * @param subjectService
     * @param name
     * @param pid
     * @return
     */
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    /**
     * 判断一级分类是否重复
     *
     * @param subjectService
     * @param name
     * @return
     */
    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    /**
     * 读取完成后执行
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 读取excel表头信息
     *
     * @param headMap
     * @param context
     */
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }
}
