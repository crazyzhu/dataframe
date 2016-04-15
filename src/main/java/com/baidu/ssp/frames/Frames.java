/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ssp.frames;

import com.baidu.ssp.Column;
import com.baidu.ssp.Frame;
import com.baidu.ssp.Order;
import com.baidu.ssp.Row;
import com.baidu.ssp.element.ListFrame;
import com.baidu.ssp.element.ListFrameColumn;
import com.baidu.ssp.element.ListFrameRow;
import com.baidu.ssp.util.Utils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * 操作Frame对象的工具类
 * @author zhuyijie
 */
public final class Frames {

    /**
     *
     * 根据目标字段的值对表中的行进行分组，类似于sql的group by功能，但聚合功能分离到aggregate函数中实现
     * @param frame
     * 原始表对象
     * @param by
     * 用于分组的字段
     * @param keyGenerator
     * 索引键生成器
     *
     * @return
     * 分组结果
     *
     * */
    public static <K> Map<K, Collection<Row>> group(Frame frame,
                                                    List<String> by,
                                                    KeyGenerator<K> keyGenerator) {
        checkState(by != null);
        checkNotNull(keyGenerator);
        Map<K, Collection<Row>> keyToDataMap = new HashMap<K, Collection<Row>>();
        ListMultimap<K, Row> resultMaps = Multimaps.newListMultimap(keyToDataMap, new Supplier<List<Row>>() {
            @Override
            public List<Row> get() {
                return new ArrayList<Row>();
            }
        });
        Iterator<Row> iterator = frame.iterator();
        List<Integer> idx = Utils.indexOf(frame.titles(), by);
        while (iterator.hasNext()) {
            Row row = iterator.next();
            K key = keyGenerator.make(Utils.select(row.values(), idx));
            resultMaps.put(key, row);
        }
        return keyToDataMap;
    }

    /**
     *
     * 实现left join功能，只支持equal join
     *
     * @param left
     * 左表
     * @param right
     * 右表
     * @param on
     * join的字段
     *
     * @return
     * join的结果
     *
     * */
    public static Frame leftJoin(final Frame left, final Frame right, List<String> on) {
        checkState(left.titles().containsAll(on));
        checkState(right.titles().containsAll(on));
        if (left.height() == 0) {
            return ListFrame.newInstance(Collections.<List<Object>>emptyList(),
                    new ArrayList<String>() {
                        {
                            addAll(left.titles());
                            addAll(right.titles());
                        }
                    });
        }
        KeyGenerator<String> keyGenerator = new StringKeyGenerator();
        // 对右表进行分组
        Map<String, Collection<Row>> rightGroup = group(right, on, keyGenerator);
        List<Row> data = new ArrayList<Row>();
        // 选取出右表中的所有字段，删去on中的字段
        final List<String> rightJoinTitles = FluentIterable.from(right.titles())
                .filter(Predicates.not(Predicates.in(on))).toList();
        for (Row leftRow : left) {
            String leftKey = makeKey(leftRow, on, keyGenerator);
            Collection<Row> rightRows = rightGroup.get(leftKey);
            if (rightRows != null) {
                for (Row rightRow : rightRows) {
                    // 左表中已经包含on字段，所以只需把右表中其余字段加上
                    data.add(leftRow.join(rightRow.select(rightJoinTitles)));
                }
            } else {
                data.add(leftRow.join(new ListFrameRow(rightJoinTitles, Arrays.asList(new Object[rightJoinTitles.size()]))));
            }
        }
        return ListFrame.fromRows(data, new ArrayList<String>(){
            {
                addAll(left.titles());
                addAll(rightJoinTitles);
            }
        });
    }
    /**
     *
     * 实现full outer join，只支持equal join
     *
     * @param left
     * 左表
     * @param right
     * 右表
     * @param on
     * join的字段
     *
     * @return
     * join的结果
     *
     * */
    public static Frame outJoin(final Frame left, final Frame right, List<String> on) {
        checkState(left.titles().containsAll(on));
        checkState(right.titles().containsAll(on));
        if (left.height() == 0 && right.height() == 0) {
            return ListFrame.newInstance(Collections.<List<Object>>emptyList(),
                    new ArrayList<String>() {
                        {
                            addAll(left.titles());
                            addAll(right.titles());
                        }
                    });
        }
        // 对右表进行风阻
        KeyGenerator<String> keyGenerator = new StringKeyGenerator();
        Map<String, Collection<Row>> rightGroup = group(right, on, keyGenerator);

        List<Row> data = new ArrayList<Row>();

        // 左右表中的非on字段
        List<String> rightJoinTitles = FluentIterable.from(right.titles())
                .filter(Predicates.not(Predicates.in(on))).toList();
        List<String> leftJoinTitles = FluentIterable.from(left.titles())
                .filter(Predicates.not(Predicates.in(on))).toList();

        // 目标结果的所有字段
        List<String> titles = Lists.newArrayList(left.titles());
        titles.addAll(rightJoinTitles);

        // 记录在中出现过的所有Key
        Set<String> walkedKeys = Sets.newHashSet();
        for (Row leftRow : left) {
            String leftKey = makeKey(leftRow, on, keyGenerator);
            walkedKeys.add(leftKey);
            Collection<Row> rightRows = rightGroup.get(leftKey);
            if (rightRows != null) {
                for (Row rightRow : rightRows) {
                    data.add(leftRow.join(rightRow.select(rightJoinTitles)));
                }
            } else {
                data.add(leftRow.join(new ListFrameRow(rightJoinTitles, Collections.nCopies(rightJoinTitles.size(), null))));
            }
        }
        // 增加右表中的剩余行
        for (Map.Entry<String, Collection<Row>> entry : rightGroup.entrySet()) {
            if (walkedKeys.contains(entry.getKey())) {
                continue;
            }
            for (Row rightRow : entry.getValue()) {
                data.add(rightRow.join(new ListFrameRow(leftJoinTitles,
                        Collections.nCopies(leftJoinTitles.size(), null))).select(titles));
            }
        }

        return ListFrame.fromRows(data, titles);

    }

    /**
     *
     * 实现right join，只支持equal join
     *
     * @param left
     * 左表
     * @param right
     * 右表
     * @param on
     * join on 的字段
     *
     * @return
     * join的结果
     *
     * */
    public static Frame rightJoin(Frame left, Frame right, List<String> on) {
        return leftJoin(right, left, on);
    }

    /**
     *
     * 实现union功能
     *
     * @param up
     * @param down
     *
     * @return
     * union的结果
     *
     * */
    public static Frame union(Frame up, Frame down) {
        if (down.height() == 0) {
            return up;
        }
        checkState(up.titles().size() == down.titles().size()
                && up.titles().containsAll(down.titles()));

        List<Row> newRows = new ArrayList<Row>(up.height() + down.height());

        for (Row upRow : up) {
            newRows.add(upRow);
        }

        for (Row downRow : down) {
            newRows.add(downRow.select(up.titles()));
        }

        return ListFrame.fromRows(newRows, up.titles());
    }

    /**
     *
     * 从Frame中过滤出满足条件的行
     *
     * @param frame
     * 待过滤的表
     * @param condition
     * 条件
     * @return
     * 过滤的结果
     *
     *
     * */
    public static Frame filter(Frame frame, Predicate<Row> condition) {
        List<Row> rows = FluentIterable.from(frame).filter(condition).toList();
        if (rows.isEmpty()) {
            return ListFrame.newInstance(Collections.<List<Object>>emptyList(), frame.titles());
        }
        return ListFrame.fromRows(rows, frame.titles());
    }


    /**
     * 用已有的字段计算得到新的字段
     *
     * @param frame
     * 数据表
     * @param title
     * 计算结果所属的字段
     * @param function
     * 计算函数
     *
     * */
    public static Frame compute(Frame frame, String title, Function<Row, Object> function) {
        checkState(!frame.titles().contains(title));
        List<Object> newValues = new ArrayList<Object>(frame.height());
        for (Row row : frame) {
            newValues.add(function.apply(row));
        }
        return frame.addColumn(new ListFrameColumn(title, newValues));
    }

    /**
     *
     * 对frame中已有的一个字段进行转化
     *
     * @param frame
     * 数据表
     * @param title
     * 列名
     * @param function
     * 转化函数
     *
     * @return
     * 转化后的结果
     *
     * */
    public static <K, V> Frame transform(Frame frame, String title, Function<K, V> function) {
        checkState(frame.titles().contains(title));
        Column column = frame.column(title);
        List<Object> newValues = new ArrayList<Object>(frame.height());
        for (Object value : column.values()) {
            newValues.add(function.apply((K) value));
        }
        return frame.removeColumn(title).addColumn(new ListFrameColumn(title, newValues));
    }

    /**
     *
     * 对frame进行排序
     *
     * @param frame
     * 数据表
     * @param orders
     * 描述顺序的对象
     *
     * @return
     * 排序后的结果
     *
     *
     * */
    public static Frame sort(Frame frame, final List<Order> orders) {
        if (frame.height() == 0) {
            return frame;
        }
        final int orderSize = orders.size();
        final List<String> titles = Lists.transform(orders, new Function<Order, String>() {
            @Override
            public String apply(Order input) {
                return input.getTitle();
            }
        });
        Ordering<Row> rowOrdering = Ordering.from(new Comparator<Row>() {
            @Override
            public int compare(Row row1, Row row2) {
                List<Object> row1Values = row1.select(titles).values();
                List<Object> row2Values = row2.select(titles).values();
                for (int i = 0;i < orderSize;i++) {
                    Object cell1 = row1Values.get(i);
                    Object cell2 = row2Values.get(i);
                    Ordering ordering = orders.get(i).getOrder();
                    int compareResult = ordering.compare(cell1, cell2);
                    if (compareResult != 0) {
                        return compareResult;
                    }
                }
                return 0;
            }
        });
        return ListFrame.fromRows(rowOrdering.immutableSortedCopy(frame), frame.titles());
    }

    /**
     * 对结果集进行切片
     *
     * @param frame
     * 数据表
     * @param begin
     * 开始选取的行号
     * @param size
     * 需要的大小
     *
     * @return
     * 切片后的结果
     *
     * */
    public static Frame limit(Frame frame, int begin, int size) {
        int height = frame.height();
        if (height <= begin || size == 0) {
            return ListFrame.newInstance(Collections.<List<Object>>emptyList(), frame.titles());
        }
        List<Row> data = new ArrayList<Row>(size);
        for (int i = 0;i < size && i + begin < height;i++) {
            data.add(frame.row(i + begin));
        }
        return ListFrame.fromRows(data, frame.titles());
    }

    /**
     *
     * 进行分组聚合计算
     *
     * @param frame
     * 数据表
     * @param by
     * 用于分组的字段
     * @param aggregates
     * 聚合函数
     *
     * @return
     * 分组计算的结果
     *
     * */
    public static Frame aggregate(Frame frame, final List<String> by, final List<AggregateFunction> aggregates) {
        checkNotNull(frame);
        checkNotNull(by);
        checkNotNull(aggregates);

        final List<String> aggregateTitles = Lists.transform(aggregates, new Function<AggregateFunction, String>() {
            @Override
            public String apply(AggregateFunction input) {
                return input.title();
            }
        });
        List<String> fullTitles = new ArrayList<String>() {
            {
                addAll(by);
                addAll(aggregateTitles);
            }
        };
        final List<AggregateFunction> byFunctions = Lists.transform(by, new Function<String, AggregateFunction>() {
            @Override
            public AggregateFunction apply(String input) {
                return Aggregates.takeAnyOne(input);
            }
        });
        List<AggregateFunction> fullFunctions = new ArrayList<AggregateFunction>() {
            {
                addAll(byFunctions);
                addAll(aggregates);
            }
        };
        checkState(frame.titles().containsAll(fullTitles));
        Map<String, Collection<Row>> groupedRows = group(frame, by, new StringKeyGenerator());
        List<Row> newRows = new ArrayList<Row>(groupedRows.size());
        for (Collection<Row> aGroup : groupedRows.values()) {
            for (AggregateFunction aFunction : fullFunctions) {
                aFunction.reset();
            }
            for (Row aRow : aGroup) {
                List<Object> values = aRow.select(fullTitles).values();
                for (int i = 0;i < values.size();i++) {
                    fullFunctions.get(i).consume(values.get(i));
                }
            }
            newRows.add(new ListFrameRow(fullTitles,
                    Lists.newArrayList(Lists.transform(fullFunctions, new Function<AggregateFunction, Object>() {
                        @Override
                        public Object apply(AggregateFunction input) {
                            return input.getResult();
                        }
                    }))));
        }
        return ListFrame.fromRows(newRows, fullTitles);
    }

    /**
     * 生成索引键
     * */
    private static <T> T makeKey(Row row, List<String> on, KeyGenerator<T> keyGenerator) {
        return keyGenerator.make(row.select(on).values());
    }

    /**
     *
     * 使用string的索引键生成器
     * @author mojie
     * @since 2015/2/5
     *
     */
    public static class StringKeyGenerator implements KeyGenerator<String> {
        @Override
        public String make(List<Object> values) {
            return Joiner.on('\u200E').useForNull("\u200F").join(values);
        }
    }

    /**
     * 组合列表中的值，得到索引键
     * @author mojie
     * @since 2015/2/5
     */
    public interface KeyGenerator<K> {
        K make(List<Object> values);
    }
}
