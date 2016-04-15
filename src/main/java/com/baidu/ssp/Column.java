/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.List;

/**
 * 列对象
 * @author zhuyijie
 */
public interface Column {

    /**
     *
     * 获取该列的名称
     *
     * */
    String title();

    /**
     *
     * 获取该列的值列表
     *
     * */
    List<Object> values();
}
