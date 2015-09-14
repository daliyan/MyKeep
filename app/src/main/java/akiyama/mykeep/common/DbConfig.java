package akiyama.mykeep.common;

/**
 * 数据库方面的配置信息
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-01  10:39
 */
public class DbConfig {

    public static final String SPLIT_SYMBOL ="&";//分割符号，用于存储在数据库中的多个组的分割，存储和解析作用
    public static final String REPLACE_SYMBOL ="& ";//转义字符串，碰到  "&"转义成"& "

    public static final String SMAIL_SPLIT_SYMBOL="&";
    public static final String BIG_SPLIT_SYMBOL=",";//分割符号，用于存储在数据库中的多个组的分割，存储和解析作用
}
