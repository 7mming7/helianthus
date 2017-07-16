package com.ha.graph.execute;

import com.ha.exception.ExecutorManagerException;
import com.ha.executor.ExecutableNode;

/**
 * User: shuiqing
 * DateTime: 17/7/13 下午3:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IExecuteLoaderService {

    void updateNode(ExecutableNode node)
            throws ExecutorManagerException;
}
