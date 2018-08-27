package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期转换工具.
 */
public class DateUtil {

    private static Date parse;
    private static Date daylightSavingTimeBegin;
    private static Date daylightSavingTimeEnd;
    private static String format = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(format);

    // 对于2018年北美中部时区的夏时令的分布情况，对夏时令开始及结束时间进行了设置.
    // 由于是日志从各个KP项目的容器中产生，而各个服务器节点都是采取服务器当地时区来运行的，因此我们需要对原时间戳转换为东八区。
    static {
        try {
            daylightSavingTimeBegin = sdf.parse("2018-03-25 01:00:00");
            daylightSavingTimeEnd = sdf.parse("2018-10-28 02:00:00");
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 秒级的时间戳转换成日期格式字符串.
     *
     * @param seconds 精确到秒的字符串
     */
    public static String timeStamp2Date(String seconds) throws ParseException {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        // 此处的seconds经测试指代的已经是北京时间
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String time = sdf.format(new Date(Long.valueOf(seconds + "000")));
        parse = sdf.parse(time);
        return sdf.format(parse);
    }

    /**
     * 转换时区.
     *
     * @param date 输入的是服务器的时区
     * @return 返回的是东八区的时间
     */
    public static String convertTimeZone(String date) throws ParseException {
        // 注意date的格式是yyyy-MM-dd HH:mm:ss，然后指代的时间是服务器本地时间
        // 注意首先转换时间戳字符串为Date类型，用来判断此时间戳是否处于夏令时的时候，若是，则更改时区为西六区，否则为西五区。
        parse = sdf.parse(date);
        // System.out.println("1: "+parse);
        if (!(parse.after(daylightSavingTimeBegin) && parse.before(daylightSavingTimeEnd))) {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        } else {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        }
        parse = sdf.parse(date);
        // System.out.println("2: "+parse);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        // System.out.println("3: "+sdf.format(parse));

        // 注意：每个时区的重新设置，只能在第一次调用有效，
        // 后面的转换依然 根据虚拟机所在地区的默认时区进行其他操作，因此对需要进行转换的动作，需要每做一次转换即设置一次。
        //parse = sdf.parse(date);
        return sdf.format(parse);
    }
}