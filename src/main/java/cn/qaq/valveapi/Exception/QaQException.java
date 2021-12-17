package cn.qaq.valveapi.Exception;

/**
 * @program: jtmcloud
 * @description:
 * @author: QAQ
 * @create: 2020-11-12 10:34
 **/
public abstract class QaQException extends Exception{
    /*无参构造函数*/
    public QaQException(){
        super();
    }

    //用详细信息指定一个异常
    public QaQException(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public QaQException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public QaQException(Throwable cause) {
        super(cause);
    }
}
