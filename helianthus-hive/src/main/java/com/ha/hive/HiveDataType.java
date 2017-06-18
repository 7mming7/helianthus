package com.ha.hive;

import com.ha.util.BytesSerializer;
import com.ha.util.BytesUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * hive 字段类型
 * User: shuiqing
 * DateTime: 17/6/15 下午4:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveDataType implements Serializable {

    private static final LinkedHashSet<String> VALID_TYPES = new LinkedHashSet<String>();

    private static Pattern TYPE_PATTERN = null;
    private static final String TYPE_PATTEN_TAIL = "\\s*" //
            + "(?:" + "[(]" + "([\\d\\s,]+)" + "[)]" + ")?";

    public static synchronized void register(String... typeNames) {
        for (String typeName : typeNames) {
            VALID_TYPES.add(typeName);
        }

        TYPE_PATTERN = Pattern.compile(//
                "(" + StringUtils.join(VALID_TYPES, "|") + ")" //
                        + TYPE_PATTEN_TAIL,
                Pattern.CASE_INSENSITIVE);
    }

    // standard sql types, ref: http://www.w3schools.com/sql/sql_datatypes_general.asp
    static {
        register("any", "char", "varchar", "string", //
                "boolean", "byte", "binary", //
                "int", "short", "long", "integer", "tinyint", "smallint", "bigint", //
                "int4", "long8", // for test only
                "float", "real", "double", "decimal", "numeric", //
                "date", "time", "datetime", "timestamp");
    }

    public static final Set<String> INTEGER_FAMILY = new HashSet<String>();
    public static final Set<String> NUMBER_FAMILY = new HashSet<String>();
    public static final Set<String> DATETIME_FAMILY = new HashSet<String>();
    public static final Set<String> STRING_FAMILY = new HashSet<String>();
    private static final Map<String, String> LEGACY_TYPE_MAP = new HashMap<String, String>();
    static {
        INTEGER_FAMILY.add("tinyint");
        INTEGER_FAMILY.add("smallint");
        INTEGER_FAMILY.add("integer");
        INTEGER_FAMILY.add("bigint");
        INTEGER_FAMILY.add("int4");
        INTEGER_FAMILY.add("long8");

        NUMBER_FAMILY.addAll(INTEGER_FAMILY);
        NUMBER_FAMILY.add("float");
        NUMBER_FAMILY.add("double");
        NUMBER_FAMILY.add("decimal");
        NUMBER_FAMILY.add("real");
        NUMBER_FAMILY.add("numeric");

        DATETIME_FAMILY.add("date");
        DATETIME_FAMILY.add("time");
        DATETIME_FAMILY.add("datetime");
        DATETIME_FAMILY.add("timestamp");

        STRING_FAMILY.add("varchar");
        STRING_FAMILY.add("char");

        LEGACY_TYPE_MAP.put("byte", "tinyint");
        LEGACY_TYPE_MAP.put("int", "integer");
        LEGACY_TYPE_MAP.put("short", "smallint");
        LEGACY_TYPE_MAP.put("long", "bigint");
        LEGACY_TYPE_MAP.put("string", "varchar");
        LEGACY_TYPE_MAP.put("hllc10", "hllc(10)");
        LEGACY_TYPE_MAP.put("hllc12", "hllc(12)");
        LEGACY_TYPE_MAP.put("hllc14", "hllc(14)");
        LEGACY_TYPE_MAP.put("hllc15", "hllc(15)");
        LEGACY_TYPE_MAP.put("hllc16", "hllc(16)");
    }

    private static final ConcurrentMap<HiveDataType, HiveDataType> CACHE = new ConcurrentHashMap<HiveDataType, HiveDataType>();

    public static final HiveDataType ANY = HiveDataType.getType("any");

    static {
        //to ensure the MeasureTypeFactory class has initialized
        /*MeasureTypeFactory.getUDAFs();*/
    }

    public static HiveDataType getType(String type) {
        if (type == null)
            return null;

        HiveDataType dataType = new HiveDataType(type);
        HiveDataType cached = CACHE.get(dataType);
        if (cached == null) {
            CACHE.put(dataType, dataType);
            cached = dataType;
        }
        return cached;
    }

    // ============================================================================

    private String name;
    private int precision;
    private int scale;

    public HiveDataType(String name, int precision, int scale) {
        this.name = name;
        this.precision = precision;
        this.scale = scale;
    }

    private HiveDataType(String datatype) {
        datatype = datatype.trim().toLowerCase();
        datatype = replaceLegacy(datatype);

        Pattern pattern = TYPE_PATTERN;
        Matcher m = pattern.matcher(datatype);
        if (m.matches() == false)
            throw new IllegalArgumentException("bad data type -- " + datatype + ", does not match " + pattern);

        name = replaceLegacy(m.group(1));
        precision = -1;
        scale = -1;

        String leftover = m.group(2);
        if (leftover != null) {
            String[] parts = leftover.split("\\s*,\\s*");
            for (int i = 0; i < parts.length; i++) {
                int n;
                try {
                    n = Integer.parseInt(parts[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("bad data type -- " + datatype + ", precision/scale not numeric");
                }
                if (i == 0)
                    precision = n;
                else if (i == 1)
                    scale = n;
                else
                    throw new IllegalArgumentException("bad data type -- " + datatype + ", too many precision/scale parts");
            }
        }

        // FIXME 256 for unknown string precision
        if ((name.equals("char") || name.equals("varchar")) && precision == -1) {
            precision = 256; // to save memory at frontend, e.g. tableau will
            // allocate memory according to this
        }

        // FIXME (19,4) for unknown decimal precision
        if ((name.equals("decimal") || name.equals("numeric")) && precision == -1) {
            precision = 19;
            scale = 4;
        }

    }

    private String replaceLegacy(String str) {
        String replace = LEGACY_TYPE_MAP.get(str);
        return replace == null ? str : replace;
    }

    public int getStorageBytesEstimate() {
        return HiveDataTypeSerializer.create(this).getStorageBytesEstimate();
    }

    public boolean isStringFamily() {
        return STRING_FAMILY.contains(name);
    }

    public boolean isIntegerFamily() {
        return INTEGER_FAMILY.contains(name);
    }

    public boolean isNumberFamily() {
        return NUMBER_FAMILY.contains(name);
    }

    public boolean isDateTimeFamily() {
        return DATETIME_FAMILY.contains(name);
    }

    public boolean isTimeFamily() {
        return DATETIME_FAMILY.contains(name) && !isDate();
    }

    public boolean isDate() {
        return name.equals("date");
    }

    public boolean isTime() {
        return name.equals("time");
    }

    public boolean isTimestamp() {
        return name.equals("timestamp");
    }

    public boolean isDatetime() {
        return name.equals("datetime");
    }

    public boolean isTinyInt() {
        return name.equals("tinyint");
    }

    public boolean isSmallInt() {
        return name.equals("smallint");
    }

    public boolean isInt() {
        return name.equals("integer");
    }

    public boolean isBigInt() {
        return name.equals("bigint");
    }

    public boolean isFloat() {
        return name.equals("float");
    }

    public boolean isDouble() {
        return name.equals("double");
    }

    public boolean isDecimal() {
        return name.equals("decimal");
    }

    public String getName() {
        return name;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + precision;
        result = prime * result + scale;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HiveDataType other = (HiveDataType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (precision != other.precision)
            return false;
        if (scale != other.scale)
            return false;
        return true;
    }

    @Override
    public String toString() {
        if (precision < 0 && scale < 0)
            return name;
        else if (scale < 0)
            return name + "(" + precision + ")";
        else
            return name + "(" + precision + "," + scale + ")";
    }

    public static final BytesSerializer<HiveDataType> serializer = new BytesSerializer<HiveDataType>() {
        @Override
        public void serialize(HiveDataType value, ByteBuffer out) {
            BytesUtil.writeUTFString(value.name, out);
            BytesUtil.writeVInt(value.precision, out);
            BytesUtil.writeVInt(value.scale, out);

        }

        @Override
        public HiveDataType deserialize(ByteBuffer in) {
            String name = BytesUtil.readUTFString(in);
            int precision = BytesUtil.readVInt(in);
            int scale = BytesUtil.readVInt(in);

            return new HiveDataType(name, precision, scale);
        }
    };
}
