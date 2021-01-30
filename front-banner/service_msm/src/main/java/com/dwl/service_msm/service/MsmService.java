package com.dwl.service_msm.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;

import java.util.List;

public interface MsmService {

    /**
     * 阿里云发送短信服务
     *
     * @param phoneNumbers 手机号
     * @param templateCode 模板编号
     */
    void send(String phoneNumbers, String templateCode);

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
