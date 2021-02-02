package com.dwl.service_msm.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;

import java.util.List;
import java.util.Map;

public interface MsmService {

    /**
     * 阿里云发送短信服务
     *  @param phone 手机号
     * @param templateCode 模板编号
     * @param param 模板中的变量
     */
    void send(String phone, String templateCode, Map<String, Object> param);

    /**
     * @param phoneNumbers 手机号
     * @param data         短信发送的日期 支持30天内记录查询
     * @param bizId        调用发送短信接口时返回的BizId（可选）
     * @param pageSize     页大小
     * @param currentPage  当前页码从1开始计数
     * @return List<QuerySendDetailsResponse.SmsSendDetailDTO>
     */
    List<QuerySendDetailsResponse.SmsSendDetailDTO> messageQuery(String phoneNumbers, String data, String bizId, long pageSize, long currentPage);
}
