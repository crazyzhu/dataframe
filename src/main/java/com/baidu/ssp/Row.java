/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp;

import java.util.List;
import java.util.Map;

/**
 *
 * 行对象
 *
 */
public interface Row {

    /**
     *
     * 获取字段列表
     *
     * @return
     * 字段列表
     *
     * */
    List<String> titles();

    /**
     *
     * 获取行的值列表
     *
     * @return
     * 值列表
     *
     * */
    List<Object> values();


    /**
     *
     * 选取行中的某些字段，生成新的一行
     *
     * @param subTitles
     * 需要选取的字段列表
     *
     * @return
     * 包含目标字段的新行
     *
     * */
    Row select(List<String> subTitles);

    /**
     *
     * 获取目标字段的值
     *
     * @param title
     * 目标字段名称
     *
     * @return
     * 目标字段的值
     *
     * */
    Object select(String title);

    /**
     *
     * 与另一行进行拼接
     *
     * @param another
     * 需要拼接的行
     *
     * @return
     * 拼接后生成的新行
     *
     * */
    Row join(Row another);

    /**
     *
     * 生成Map的视图
     *
     * @return
     * 生成的Map视图
     *
     * */
    Map<String, Object> mapView();
}
