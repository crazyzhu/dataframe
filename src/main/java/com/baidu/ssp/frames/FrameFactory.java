/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp.frames;

import com.baidu.ssp.Frame;
import com.baidu.ssp.element.ListFrame;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mojie on 2015/2/12.
 */
public final class FrameFactory {

    private static final Function<Field, String> field2Title = new Function<Field, String>() {
        @Override
        public String apply(Field input) {
            return input.getName();
        }
    };

    public static <T> Frame fromBeans(List<T> beans, Class<T> type) {
        final List<Field> fields = Arrays.asList(type.getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
        }
        final List<String> titles = Lists.transform(fields, field2Title);
        Function<T, List<Object>> bean2Row = new Function<T, List<Object>>() {
            @Override
            public List<Object> apply(T input) {
                List<Object> result = new ArrayList<Object>();
                for (int i = 0;i < fields.size();i++) {
                    try {
                        result.add(fields.get(i).get(input));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                return result;
            }
        };
        return ListFrame.newInstance(Lists.transform(beans, bean2Row), titles);
    }
}
