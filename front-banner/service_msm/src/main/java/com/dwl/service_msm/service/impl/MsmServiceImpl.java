package com.dwl.service_msm.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dwl.common_utils.Result.ResultCode;
import com.dwl.common_utils.util.StringUtil;
import com.dwl.service_base.exception_handler.GuLiException;
import com.dwl.service_msm.msmUtil.ConstantPropertiesUtil;
import com.dwl.service_msm.service.MsmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 阿里云短信服务签名无法申请下来！这个功能暂定使用。
 */
@Service
public class MsmServiceImpl implements MsmService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MsmServiceImpl.class);

    private final String accessKeyId = ConstantPropertiesUtil.KEY_ID; // 你的accessKeyId，参考本文档步骤2
    private final String accessKeySecret = ConstantPropertiesUtil.KEY_SECRET; // 你的accessKeySecret，参考本文档步骤2

    /**
     * 阿里云发送短信服务
     *
     * @param phoneNumbers 手机号
     * @param templateCode 模板编号
     */
    public void send(String phoneNumbers, String templateCode) {

        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "50000");
        System.setProperty("sun.net.client.defaultReadTimeout", "50000");
        // 初始化ascClient需要的几个参数
        final String product = "Dysmsapi"; // 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com"; // 短信API产品域名（接口地址固定，无需修改）
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessKeyId,
                this.accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            // 组装请求对象
            SendSmsRequest request = new SendSmsRequest();

            // 使用post提交
            request.setMethod(MethodType.POST);

            /* 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,
            批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；
            发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000” */
            request.setPhoneNumbers(phoneNumbers);

            // 必填:短信签名-可在短信控制台中找到
            request.setSignName("云通信");

            // 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(templateCode);

            /* 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,
                比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
            request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}"); */

            /* 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            request.setSmsUpExtendCode("90997"); */

            /* 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId"); */

            // 请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            LOGGER.info("用户发送短信后的bizId：{}", sendSmsResponse.getBizId());
            if (StringUtil.isEmpty(sendSmsResponse.getCode()) && !sendSmsResponse.getCode().equals("OK")) {
                LOGGER.error("用户手机 [{}]短信发送失败！也没有抛出异常！请检查数据！", phoneNumbers);
                throw new GuLiException(ResultCode.MESSAGES_ERROR.getStatus(),
                        "用户手机 [" + phoneNumbers + "]短信发送失败！也没有抛出异常！请检查数据！");
            }
        } catch (ClientException e) {
            LOGGER.error("用户手机 [{}] 短信发送失败！异常信息为：[{}]", phoneNumbers, e);
            throw new GuLiException(ResultCode.MESSAGES_ERROR.getStatus(), e + "");
        }
    }

    /**
     * @param phoneNumbers 手机号
     * @param data         短信发送的日期 支持30天内记录查询
     * @param bizId        调用发送短信接口时返回的BizId（可选）
     * @param pageSize     页大小
     * @param currentPage  当前页码从1开始计数
     * @return boolean
     */
    public List<com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO> messageQuery(String phoneNumbers, String data, String bizId, long pageSize, long currentPage) {
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //云通信产品-短信API服务产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";

        //云通信产品-短信API服务产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";

        //初始化ascClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessKeyId, this.accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

            IAcsClient acsClient = new DefaultAcsClient(profile);

            // 组装请求对象
            QuerySendDetailsRequest request = new QuerySendDetailsRequest();

            // 必填-号码
            request.setPhoneNumber(phoneNumbers);

            // 可选-调用发送短信接口时返回的BizId
            request.setBizId(bizId);

            // 必填-短信发送的日期 支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
            request.setSendDate(data);

            // 必填-页大小
            request.setPageSize(pageSize);

            // 必填-当前页码从1开始计数
            request.setCurrentPage(currentPage);

            // hint 此处可能会抛出异常，注意catch
            QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
            // 获取返回结果
            if (querySendDetailsResponse.getCode() != null && querySendDetailsResponse.getCode().equals("OK")) {
                // 可用于获取发送短信的细节
                List<com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse.SmsSendDetailDTO> list = querySendDetailsResponse.getSmsSendDetailDTOs();
                return list;
            } else {
                throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), "获取短信列表失败！");
            }
        } catch (ClientException e) {
            throw new GuLiException(ResultCode.QUERY_ERROR.getStatus(), e + "");
        }
    }
}
