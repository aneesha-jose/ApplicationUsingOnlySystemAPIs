package com.aneeshajose.headlines.network;

import java.util.Map;

/**
 * Created by Aneesha on 13/10/17.
 */

public interface ApiCallbacks {

    <T> void onSuccess(@CallTags.CallIdentifiers String callTag, T response, Map<String, Object> extras);

    void onError(@CallTags.CallIdentifiers String callTag, Throwable e, Map<String, Object> extras);

}
