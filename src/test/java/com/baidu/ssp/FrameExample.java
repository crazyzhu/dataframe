package com.baidu.ssp;

import com.baidu.ssp.frames.Aggregates;
import com.baidu.ssp.frames.FluentFrame;
import com.baidu.ssp.frames.FramePredicates;
import com.google.common.base.Function;
import com.google.common.base.Predicates;

import java.util.Arrays;

import static com.baidu.ssp.FrameFactory.makeFrame;
/**
 * Frame的例子
 * @author mojie
 * @since 2015/3/25
 */
public class FrameExample {
    public static void main(String[] args) {
        Frame frame1 = makeFrame(
                new String[]
                        {"userId", "adPositionId", "view"},
                new Object[][] {
                        {1, 1001, 100},
                        {1, 1002, 101},
                        {2, 1003, 102}
                }
        );
        System.out.println("frame1 的内容为:\n" + frame1);
        //frame1 的内容为:
        //userId     view       adPositionId
        //1          100        1001
        //1          101        1002
        //2          102        1003
        Frame frame2 = makeFrame(
                new String[]
                        {"userId", "adPositionId", "click"},
                new Object[][] {
                        {1, 1001, 10},
                        {1, 1002, 11},
                        {2, 1003, 12}
                }
        );
        //frame2 的内容为:
        //userId     click      adPositionId
        //1          10         1001
        //1          11         1002
        //2          12         1003
        System.out.println("frame2 的内容为:\n" + frame2);

        // 创建链式调用对象
        FluentFrame fluentFrame = FluentFrame.from(frame1);

        // 即查出userId=1的用户的view的总数
        Frame result = fluentFrame
                .aggregate(Arrays.asList("userId"), Arrays.asList(Aggregates.sum("view")))
                .filter(FramePredicates.onTitle("userId", Predicates.equalTo(1)))
                .toFrame();
        System.out.println("result:\n" + result);
        //result:
        //userId     view
        //1          201
        // 根据userId与adPositionId进行两张表的join
        fluentFrame = FluentFrame.from(frame1);
        result = fluentFrame.leftJoin(frame2, Arrays.asList("userId", "adPositionId")).toFrame();

        System.out.println("result:\n" + result);
        //result:
        //userId     click      adPositionId view
        //1          10         1001       100
        //1          11         1002       101
        //2          12         1003       102

        // 根据userId与adPositionId进行两张表的join，
        // 计算点击率并进行format
        fluentFrame = FluentFrame.from(frame1);
        result = fluentFrame
                .leftJoin(frame2, Arrays.asList("userId", "adPositionId"))
                .compute("clickRatio", new Function<Row, Object>() {
                    @Override
                    public Object apply(Row input) {
                        Integer view = (Integer) input.select("view");
                        Integer click = (Integer) input.select("click");
                        return click.doubleValue() / view.doubleValue() * 100;
                    }
                }).transform("clickRatio", new Function<Object, Object>() {
                    @Override
                    public Object apply(Object input) {
                        return String.format("%.2f%%", (Double) input);
                    }
                }).toFrame();

        System.out.println("result:\n" + result);
        //result:
        //userId     click      view       adPositionId clickRatio
        //1          10         100        1001       10.00%
        //1          11         101        1002       10.89%
        //2          12         102        1003       11.76%
    }
}
