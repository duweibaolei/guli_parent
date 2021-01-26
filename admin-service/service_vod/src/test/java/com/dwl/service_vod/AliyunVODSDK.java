package com.dwl.service_vod;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;

import java.util.List;

public class AliyunVODSDK {

    private static final String ACCESS_KEY_ID = "LTAI4GJipWohDUs9wCags23R";
    private static final String ACCESS_KEY_SECRET = "MRWBTbAPAm1Bhu104Wrdgk1QQJI2ZM";

    /**
     * 初始化客户端
     *
     * @return
     * @throws ClientException
     */
    public static DefaultAcsClient initVodClient() throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 测试初始化客户端
     *
     * @throws ClientException
     */
    @Test
    public void testGetVideoPlayAuth() throws ClientException {
        DefaultAcsClient client = initVodClient();
        System.out.println(client.toString() + "------------client");
    }

    /**
     * 刷新视频上传凭证
     *
     * @param client 发送请求客户端
     * @return RefreshUploadVideoResponse 刷新视频上传凭证响应数据
     * @throws Exception
     */
    public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient client) throws Exception {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId("dd8846ee50784f75811add6fe9dc4b14");
        return client.getAcsResponse(request);
    }

    /**
     * 测试视频上传凭证刷新
     *
     * @throws ClientException
     */
    @Test
    public void testRefreshUploadVideoResponse() throws ClientException {
        DefaultAcsClient client = initVodClient();
        RefreshUploadVideoResponse response = new RefreshUploadVideoResponse();
        try {
            response = refreshUploadVideo(client);
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    /**
     * 获取播放地址函数
     *
     * @param client
     * @return
     * @throws Exception
     */
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("dd8846ee50784f75811add6fe9dc4b14");
        return client.getAcsResponse(request);
    }

    /**
     * 测试获取播放地址函数
     */
    @Test
    public void testGetPlayInfo() throws ClientException {
        DefaultAcsClient client = initVodClient();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = getPlayInfo(client);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    /**
     * 获取播放凭证函数
     *
     * @param client
     * @return
     * @throws Exception
     */
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId("dd8846ee50784f75811add6fe9dc4b14");
        return client.getAcsResponse(request);
    }

    /**
     * 获取播放凭证函数
     */
    @Test
    public void tsetGetVideoPlayAuth() throws ClientException {
        DefaultAcsClient client = initVodClient();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            response = getVideoPlayAuth(client);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
