/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp.element;

import com.baidu.ssp.Column;

import java.util.List;

/**
 * ʹ���б�ʵ�ֵ��ж���
 */
public class ListFrameColumn implements Column {
    // ����
    private String title;
    // ֵ�б�
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
