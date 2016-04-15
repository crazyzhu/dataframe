/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.List;
import java.util.Map;

/**
 *
 * �ж���
 *
 */
public interface Row {

    /**
     *
     * ��ȡ�ֶ��б�
     *
     * @return
     * �ֶ��б�
     *
     * */
    List<String> titles();

    /**
     *
     * ��ȡ�е�ֵ�б�
     *
     * @return
     * ֵ�б�
     *
     * */
    List<Object> values();


    /**
     *
     * ѡȡ���е�ĳЩ�ֶΣ������µ�һ��
     *
     * @param subTitles
     * ��Ҫѡȡ���ֶ��б�
     *
     * @return
     * ����Ŀ���ֶε�����
     *
     * */
    Row select(List<String> subTitles);

    /**
     *
     * ��ȡĿ���ֶε�ֵ
     *
     * @param title
     * Ŀ���ֶ�����
     *
     * @return
     * Ŀ���ֶε�ֵ
     *
     * */
    Object select(String title);

    /**
     *
     * ����һ�н���ƴ��
     *
     * @param another
     * ��Ҫƴ�ӵ���
     *
     * @return
     * ƴ�Ӻ����ɵ�����
     *
     * */
    Row join(Row another);

    /**
     *
     * ����Map����ͼ
     *
     * @return
     * ���ɵ�Map��ͼ
     *
     * */
    Map<String, Object> mapView();
}
