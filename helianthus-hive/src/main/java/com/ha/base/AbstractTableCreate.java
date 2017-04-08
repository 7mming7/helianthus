package com.ha.base;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * User: shuiqing
 * DateTime: 17/4/7 下午5:40
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public abstract class AbstractTableCreate implements TableOperation {

    @Override
    public TableInfo recreate(OutputStream out,String table) throws IOException, URISyntaxException {
        TableInfo tiD = this.delete(out,table);
        TableInfo tiC = this.create(out,table);
        tiD.join(tiC);
        return tiD;
    }

    @Override
    public TableInfo reCreateAll(OutputStream out) throws IOException, URISyntaxException  {
        TableInfo tiD = this.deleteAll(out);
        TableInfo tiC = this.createAll(out);
        tiD.join(tiC);
        return tiD;
    }
}
