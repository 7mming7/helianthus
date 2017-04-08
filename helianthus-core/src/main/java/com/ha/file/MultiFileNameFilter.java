package com.ha.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多文件名过滤
 * User: shuiqing
 * DateTime: 17/4/7 下午2:11
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class MultiFileNameFilter implements FilenameFilter {

    public static final FilenameFilter hiddenFileFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return !name.startsWith(".");
        }
    };

    private List<FilenameFilter> filters = new ArrayList<FilenameFilter>();

    public MultiFileNameFilter(){}


    public void addFilter(FilenameFilter filter){
        this.filters.add(filter);
    }

    public MultiFileNameFilter(List<FilenameFilter> filters) {
        this.filters = filters;
    }

    /**
     * 添加隐藏文件过滤
     */
    public MultiFileNameFilter addHiddenFileFilter(){
        this.filters.add(hiddenFileFilter);
        return this;
    }

    @Override
    public boolean accept(File dir, String name) {
        for (FilenameFilter filter : filters) {
            if (!filter.accept(dir, name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 得到一个过滤器
     * @param filenameFilters
     * @return
     */
    public static final MultiFileNameFilter get(FilenameFilter... filenameFilters){
        MultiFileNameFilter mff = new MultiFileNameFilter();
        mff.filters.addAll(Arrays.asList(filenameFilters));
        return mff;
    }
}
