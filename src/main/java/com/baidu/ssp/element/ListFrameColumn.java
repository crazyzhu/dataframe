/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp.element;

import com.baidu.ssp.Column;

import java.util.List;

/**
 * 使用列表实现的列对象
 */
public class ListFrameColumn implements Column {
    // 列名
    private String title;
    // 值列表
    private List<Object> values;

    public ListFrameColumn(String title, List<Object> values) {
        this.title = title;
        this.values = values;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public List<Object> values() {
        return this.values;
    }
}
