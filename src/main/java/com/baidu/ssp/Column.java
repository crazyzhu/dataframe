/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.List;

/**
 * �ж���
 * @author zhuyijie
 */
public interface Column {

    /**
     *
     * ��ȡ���е�����
     *
     * */
    String title();

    /**
     *
     * ��ȡ���е�ֵ�б�
     *
     * */
    List<Object> values();
}
