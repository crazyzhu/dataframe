/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp.frames;

import com.baidu.ssp.Frame;
import com.baidu.ssp.Order;
import com.baidu.ssp.Row;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.List;

/**
 * ����Frame����ʽ�ӿ�
 * @author zhuyijie
 */
public final class FluentFrame {

    private Frame innerFrame;

    private FluentFrame(Frame innerFrame) {
        this.innerFrame = innerFrame;
    }

    /**
     * ����FluentFrame
     *
     * @param frame
     * ���ݱ�
     *
     * @return
     * ��ʽFrame
     * */
    public static FluentFrame from(Frame frame) {
        return new FluentFrame(frame);
    }

    /**
     *
     * ����һ��Frame
     *
     * @param other
     * �����ӵ�Ŀ��Frame
     * @param on
     * �����ֶ�
     *
     * @return
     * ���FluentFrame
     *
     * */
    public FluentFrame leftJoin(Frame other, List<String> on) {
        innerFrame = Frames.leftJoin(innerFrame, other, on);
        return this;
    }
    /**
     *
     * full join һ��Frame
     *
     * @param other
     * �����ӵ�Ŀ��Frame
     * @param on
     * �����ֶ�
     *
     * @return
     * ��ʽFrame
     *
     * */
    public FluentFrame outJoin(Frame other, List<String> on) {
        innerFrame = Frames.outJoin(innerFrame, other, on);
        return this;
    }

    /**
     *
     * ����һ��Frame
     *
     * @param other
     * �����ӵ�Ŀ��Frame
     * @param on
     * �����ֶ�
     *
     * @return
     * ��ʽFrame
     *
     * */
    public FluentFrame rightJoin(Frame other, List<String> on) {
        innerFrame = Frames.rightJoin(innerFrame, other, on);
        return this;
    }

    /**
     *
     * �����ݱ��������
     *
     * @param orders
     * �����ֶ�
     *
     * @return
     * ��ʽFrame
     *
     * */
    public FluentFrame sort(List<Order> orders) {
        innerFrame = Frames.sort(innerFrame, orders);
        return this;
    }

    /**
     *
     * �����ݱ���й���
     *
     * @param condition
     * �����ֶ�
     *
     * @return
     * ��ʽFrame
     *
     * */
    public FluentFrame filter(Predicate<Row> condition) {
        innerFrame = Frames.filter(innerFrame, condition);
        return this;
    }
    /**
     *
     * ������һ��Frame����union
     *
     * @param other
     * ��union��Frame
     *
     * @return
     * union�Ľ��
     *
     * */
    public FluentFrame union(Frame other) {
        innerFrame = Frames.union(innerFrame, other);
        return this;
    }

    /**
     *
     * ���з���ۺϼ���
     *
     * @param by
     * �����ֶ�
     * @param aggregates
     * �ۺϺ���
     *
     * @return
     * �������
     *
     * */
    public FluentFrame aggregate(List<String> by, List<AggregateFunction> aggregates) {
        innerFrame = Frames.aggregate(innerFrame, by, aggregates);
        return this;
    }

    /**
     *
     * ����ԭ�б��е�ֵ����õ���ֵ
     *
     * @param title
     * ��ֵ���ֶ�
     * @param function
     * ���㺯��
     *
     * @return
     * ��������������ʽFrame
     *
     * */
    public FluentFrame compute(String title, Function<Row, Object> function) {
        innerFrame = Frames.compute(innerFrame, title, function);
        return this;
    }

    /**
     *
     * ��һ���ֶν���ת��
     *
     * @param title
     * ��Ҫ����ת�����ֶε�ֵ
     * @param function
     * ת������
     *
     * @return
     * ת����Ľ��
     *
     * */
    public <K, V> FluentFrame transform(String title, Function<K, V> function) {
        innerFrame = Frames.transform(innerFrame, title, function);
        return this;
    }

    /**
     *
     * �Խ��������Ƭ
     *
     * @param begin
     * ��Ƭ�Ľ���к�
     * @param size
     * ������Ĵ�С
     *
     * @return
     * ��Ƭ��Ľ��
     *
     * */
    public FluentFrame limit(int begin, int size) {
        innerFrame = Frames.limit(innerFrame, begin, size);
        return this;
    }

    /**
     *
     * ת����Frame����
     *
     * */
    public Frame toFrame() {
        return innerFrame;
    }
}
