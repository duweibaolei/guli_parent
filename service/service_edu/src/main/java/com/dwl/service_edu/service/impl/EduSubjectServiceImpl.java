package com.dwl.service_edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dwl.common_utils.BeanUtil;
import com.dwl.common_utils.ResultCode;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_edu.config.SubjectExcelListener;
import com.dwl.service_edu.entity.EduSubject;
import com.dwl.service_edu.entity.excel.ExcelSubjectData;
import com.dwl.service_edu.entity.vo.SubjectNestedVo;
import com.dwl.service_edu.entity.vo.SubjectVo;
import com.dwl.service_edu.mapper.EduSubjectMapper;
import com.dwl.service_edu.service.EduSubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author dwl
 * @since 2021-01-13
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EduSubjectServiceImpl.class);

    /**
     * Excel批量导入
     *
     * @param file
     * @param eduSubjectService
     */
    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            LOGGER.error("Excel批量导入异常：{}", e.getMessage());
            throw new GuLiException(ResultCode.SAVE_ERROR.getStatus(), "添加课程分类失败!");
        }
    }

    /**
     * 课程分类列表（树形）
     *
     * @return
     */
    @Override
    public List<SubjectNestedVo> nestedList() {
        // 最终要得到的数据列表
        ArrayList<SubjectNestedVo> subjectNestedVoArrayList = new ArrayList<>();

        // 获取一级分类数据记录
        QueryWrapper<EduSubject> queryWrapperOne = new QueryWrapper<>();
        queryWrapperOne.eq("parent_id", 0);
        queryWrapperOne.orderByAsc("sort", "id");
        List<EduSubject> subjects = baseMapper.selectList(queryWrapperOne);

        // 获取二级分类数据记录
        QueryWrapper<EduSubject> queryWrapperTwo = new QueryWrapper<>();
        queryWrapperTwo.ne("parent_id", 0);
        queryWrapperTwo.orderByAsc("sort", "id");
        List<EduSubject> subSubjects = baseMapper.selectList(queryWrapperTwo);

        //填充一级分类vo数据
        int count = subjects.size();
        for (int i = 0; i < count; i++) {
            EduSubject subject = subjects.get(i);
            // 创建一级类别vo对象
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            BeanUtils.copyProperties(subject, subjectNestedVo);
            subjectNestedVoArrayList.add(subjectNestedVo);
            // 填充二级分类vo数据
            ArrayList<SubjectVo> subjectVoArrayList = new ArrayList<>();
            int countTwo = subSubjects.size();
            for (int j = 0; j < countTwo; j++) {
                EduSubject subSubject = subSubjects.get(j);
                if (subject.getId().equals(subSubject.getParentId())) {
                    // 创建二级类别vo对象
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subSubject, subjectVo);
                    subjectVoArrayList.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(subjectVoArrayList);
        }
        return subjectNestedVoArrayList;
    }


}
