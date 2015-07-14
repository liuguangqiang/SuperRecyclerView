/*
 * Copyright 2015 Eric Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liuguangqiang.recyclerview.sample;

/**
 * Created by Eric on 15/6/6.
 */
public class ApiUtils {

    private static final String HOST_NAME = "http://news-at.zhihu.com/api/4";
    
    private static final String FORMAT = HOST_NAME + "/%s";

    public static String getLatest() {
        return String.format(FORMAT, "news/latest");
    }

    public static String getNewsBefore(int datetime) {
        String action = String.format("stories/before/%d?client=0", datetime);
        return String.format(FORMAT, action);
    }

    public static String getStory(int id) {
        String action = String.format("story/%d", id);
        return String.format(FORMAT, action);
    }

}
