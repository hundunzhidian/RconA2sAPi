package cn.qaq.valveapi.utils;

/**
 * <p>
 * 标题: HuangException.java
 * </p>
 * <p>
 * 描述:
 * </p>
 * <p>
 * 版权: HUANG All Right
 * </p>
 * <p>
 * 创建时间: 2018-6-26
 * </p>
 * <p>
 * 作者: HUANG
 * </p>
 * <p>修改历史记录：</p>
 * ====================================================================<br>
 * 维护单：<br>
 * 修改日期：<br>
 * 修改人：<br>
 * 修改内容：<br>
 */
public class HuangException extends Exception {
    //无参构造方法
    public HuangException() {
        super();
    }

    //有参的构造方法
    public HuangException(String message) {
        super(message);
    }

    // 用指定的详细信息和原因构造一个新的异常
    public HuangException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public HuangException(Throwable cause) {
        super(cause);
    }
}
