package etl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import util.ConvertBase;
import util.DateUtil;

import java.io.IOException;

public class BaseConvert extends ConvertBase {

    /**
     * 清洗子类的构造函数，用来传入日志清洗的输入输出流.
     *
     * @param input 输入数据的路径
     * @param output 输出数据的路径
     * @throws IOException I/O异常
     */
    public BaseConvert(String input, String output) throws IOException {
        super(input, output);
        br = super.getBr();
        bw = super.getBw();
    }

    /**
     * JSON格式清洗.
     */
    public void jsonConvert() throws IOException {
        String line;
        String ws;
        JsonObject object;
        int count = 0;

        while ((line = br.readLine()) != null) {
            JsonParser parser = new JsonParser();
            try {
                count++;
                // 创建一个包含原始json串的json对象
                //object = (JsonObject) parser.parse(line);
                object = (JsonObject) parser.parse(new String(line.getBytes("UTF-8"),"UTF-8"));
                ws = object.toString();
                bw.write(ws);
                bw.newLine();
                bw.flush();
                if (count == 607){
                    System.out.println(line);
                }
            } catch (Exception format) {
                System.out.println("[Error Line " + count + "] >>>>>> " + line);
            }
        }
        br.close();
        bw.close();
        //^^^
    }
}