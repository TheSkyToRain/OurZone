package com.ebaryice.ourzone;

import java.util.List;

import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by Ebaryice on 2017/11/8.
 */

public interface LCChatProfileProvider {
    // 根据传入的 clientId list，查找、返回用户的 Profile 信息(id、昵称、头像)
    public void fetchProfiles(List<String> userIdList, LCChatProfilesCallBack profilesCallBack);
}
