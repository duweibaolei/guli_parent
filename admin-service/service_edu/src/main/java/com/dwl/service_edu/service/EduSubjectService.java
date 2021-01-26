package com.dwl.service_edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dwl.service_edu.entity.EduSubject;
import com.dwl.service_edu.entity.vo.SubjectNestedVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author dwl
 * @since 2021-01-13
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * Excel批量导入
     *
     * @param file
     * @param eduSubjectService
     */
    void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService);

    /**
     * 嵌套数据列表
     *
     * @return
     */
    List<SubjectNestedVo> nestedList();

}
