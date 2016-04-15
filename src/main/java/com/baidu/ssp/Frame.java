/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.Iterator;
import java.util.List;

/**
 *
 * ���ݱ����
 *
 * @author zhuyijie
 */
public interface Frame extends Iterable<Row> {
    /**
     * ��ȡ���ĸ߶ȣ��������е�����
     *
     * @return
     * ���ĸ߶�
     *
     * */
    int height();

    /**
     *
     * ��ȡ������������
     *
     * @return �����б�
     *
     * */
    List<String> titles();

    /**
     *
     * ��ȡ�ж���
     *
     * @param index
     * �к�
     *
     * @return
     * �ж���
     *
     * */
    Row row(int index);

    /**
     *
     * ��ȡ�ж���
     *
     * @param title
     * ����
     *
     * @return
     * �ж���
     *
     * */
    Column column(String title);

    /**
     * ��Frame������һ��
     *
     * @param column
     * �����ӵ���
     *
     * @return
     * ���º��Frame
     * */
    Frame addColumn(Column column);

    /**
     *
     * ɾ��Frame�е�ĳ��
     *
     * @param title
     * ��ɾ�����е�����
     *
     * @return
     * ���º��Frame
     *
     * */
    Frame removeColumn(String title);

    /**
     *
     * ѡȡFrame�е�ĳЩ��
     *
     * @param titles
     * ��ѡȡ������
     *
     * @return
     * ����ָ������ѡȡ�õ���Frame
     * */
    Frame select(List<String> titles);

    /**
     *
     * �޸�����
     * @param src
     * ԭʼ����
     * @param dest
     * Ŀ������
     *
     * @return
     * �޸�������õ���Frame
     *
     * */
    Frame rename(String src, String dest);

    /**
     *
     * ��ȡ�е�����
     *
     * @return
     * �е�����
     *
     * */
    Iterator<Row> iterator();

}
