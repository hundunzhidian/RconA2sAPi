package cn.qaq.valveapi.Exception;

/**
 * @program: jtmcloud
 * @description:
 * @author: QAQ
 * @create: 2021-01-13 09:21
 **/
public class QaQServiceException extends QaQException {
    public QaQServiceException(){
        super();
    }
    //用详细信息指定一个异常
    public QaQServiceException(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public QaQServiceException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public QaQServiceException(Throwable cause) {
        super(cause);
    }
}