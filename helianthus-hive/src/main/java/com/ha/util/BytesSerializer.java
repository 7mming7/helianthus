package com.ha.util;

import java.nio.ByteBuffer;

/**
 * Bytes 序列化
 * User: shuiqing
 * DateTime: 17/6/15 下午4:47
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface BytesSerializer<T> {

    int SERIALIZE_BUFFER_SIZE = 65536;

    void serialize(T value, ByteBuffer out);

    T deserialize(ByteBuffer in);
}
