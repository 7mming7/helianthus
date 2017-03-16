package com.ha.util;

import java.io.*;

/**
 * 文本分割工具
 * User: shuiqing
 * DateTime: 17/3/14 下午5:46
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class SplitTool {

    public static void main(String[] args) throws IOException {
        String usage = "Usage : SplitTool <in> <out>";
        if(args.length < 2){
            System.out.println(usage);
            System.exit(-1);
        }

        String in = args[0];
        String out = args[1];
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
        FileWriter fw = new FileWriter(new File(out));
        String line = br.readLine();
        while(line != null){
            String newLine = line.replace(' ', '\001');
            fw.append(newLine).append('\n');
        }
        br.close();
        fw.close();
    }
}