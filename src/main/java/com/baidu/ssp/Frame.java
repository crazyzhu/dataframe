/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.Iterator;
import java.util.List;

/**
 *
 * 数据表对象
 *
 * @author zhuyijie
 */
public interface Frame extends Iterable<Row> {
    /**
     * 获取表格的高度，即包含行的数量
     *
     * @return
     * 表格的高度
     *
     * */
    int height();

    /**
     *
     * 获取表格的所有列名
     *
     * @return 列名列表
     *
     * */
    List<String> titles();

    /**
     *
     * 获取行对象
     *
     * @param index
     * 行号
     *
     * @return
     * 行对象
     *
     * */
    Row row(int index);

    /**
     *
     * 获取列对象
     *
     * @param title
     * 列名
     *
     * @return
     * 列对象
     *
     * */
    Column column(String title);

    /**
     * 向Frame中增加一列
     *
     * @param column
     * 待增加的列
     *
     * @return
     * 更新后的Frame
     * */
    Frame addColumn(Column column);

    /**
     *
     * 删除Frame中的某列
     *
     * @param title
     * 待删除的列的列名
     *
     * @return
     * 更新后的Frame
     *
     * */
    Frame removeColumn(String title);

    /**
     *
     * 选取Frame中的某些列
     *
     * @param titles
     * 待选取的列名
     *
     * @return
     * 根据指定列名选取得到的Frame
     * */
    Frame select(List<String> titles);

    /**
     *
     * 修改列名
     * @param src
     * 原始列名
     * @param dest
     * 目标列名
     *
     * @return
     * 修改列名后得到的Frame
     *
     * */
    Frame rename(String src, String dest);

    /**
     *
     * 获取行迭代器
     *
     * @return
     * 行迭代器
     *
     * */
    Iterator<Row> iterator();

}
