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
 * 操作Frame的流式接口
 * @author zhuyijie
 */
public final class FluentFrame {

    private Frame innerFrame;

    private FluentFrame(Frame innerFrame) {
        this.innerFrame = innerFrame;
    }

    /**
     * 构建FluentFrame
     *
     * @param frame
     * 数据表
     *
     * @return
     * 流式Frame
     * */
    public static FluentFrame from(Frame frame) {
        return new FluentFrame(frame);
    }

    /**
     *
     * 左连一个Frame
     *
     * @param other
     * 待连接的目标Frame
     * @param on
     * 连接字段
     *
     * @return
     * 结果FluentFrame
     *
     * */
    public FluentFrame leftJoin(Frame other, List<String> on) {
        innerFrame = Frames.leftJoin(innerFrame, other, on);
        return this;
    }
    /**
     *
     * full join 一个Frame
     *
     * @param other
     * 待连接的目标Frame
     * @param on
     * 连接字段
     *
     * @return
     * 流式Frame
     *
     * */
    public FluentFrame outJoin(Frame other, List<String> on) {
        innerFrame = Frames.outJoin(innerFrame, other, on);
        return this;
    }

    /**
     *
     * 右连一个Frame
     *
     * @param other
     * 待连接的目标Frame
     * @param on
     * 连接字段
     *
     * @return
     * 流式Frame
     *
     * */
    public FluentFrame rightJoin(Frame other, List<String> on) {
        innerFrame = Frames.rightJoin(innerFrame, other, on);
        return this;
    }

    /**
     *
     * 对数据表进行排序
     *
     * @param orders
     * 排序字段
     *
     * @return
     * 流式Frame
     *
     * */
    public FluentFrame sort(List<Order> orders) {
        innerFrame = Frames.sort(innerFrame, orders);
        return this;
    }

    /**
     *
     * 对数据表进行过滤
     *
     * @param condition
     * 排序字段
     *
     * @return
     * 流式Frame
     *
     * */
    public FluentFrame filter(Predicate<Row> condition) {
        innerFrame = Frames.filter(innerFrame, condition);
        return this;
    }
    /**
     *
     * 与另外一个Frame进行union
     *
     * @param other
     * 待union的Frame
     *
     * @return
     * union的结果
     *
     * */
    public FluentFrame union(Frame other) {
        innerFrame = Frames.union(innerFrame, other);
        return this;
    }

    /**
     *
     * 进行分组聚合计算
     *
     * @param by
     * 分组字段
     * @param aggregates
     * 聚合函数
     *
     * @return
     * 计算后结果
     *
     * */
    public FluentFrame aggregate(List<String> by, List<AggregateFunction> aggregates) {
        innerFrame = Frames.aggregate(innerFrame, by, aggregates);
        return this;
    }

    /**
     *
     * 根据原有表中的值计算得到新值
     *
     * @param title
     * 新值的字段
     * @param function
     * 计算函数
     *
     * @return
     * 包含计算结果的流式Frame
     *
     * */
    public FluentFrame compute(String title, Function<Row, Object> function) {
        innerFrame = Frames.compute(innerFrame, title, function);
        return this;
    }

    /**
     *
     * 对一个字段进行转化
     *
     * @param title
     * 需要进行转化的字段的值
     * @param function
     * 转化函数
     *
     * @return
     * 转化后的结果
     *
     * */
    public <K, V> FluentFrame transform(String title, Function<K, V> function) {
        innerFrame = Frames.transform(innerFrame, title, function);
        return this;
    }

    /**
     *
     * 对结果进行切片
     *
     * @param begin
     * 切片的结果行号
     * @param size
     * 结果集的大小
     *
     * @return
     * 切片后的结果
     *
     * */
    public FluentFrame limit(int begin, int size) {
        innerFrame = Frames.limit(innerFrame, begin, size);
        return this;
    }

    /**
     *
     * 转化成Frame对象
     *
     * */
    public Frame toFrame() {
        return innerFrame;
    }
}
