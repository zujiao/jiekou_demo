package com.ai.zop.bean.base;

public class ReqObj<T> {
    /**
     * 报文头
     * 放置系统参数
     */
    private ReqHeadBean head;
    /**
     * 报文体
     * 放置业务参数
     */
    private  T  body;
    /**
     * 报文附加信息
     */
    private AttachedBean attached;
    public ReqHeadBean getHead() {
        return head;
    }
    public void setHead(ReqHeadBean head) {
        this.head = head;
    }
    public   T getBody() {
        return body;
    }
    public <X> X getBody(Class<X> cls) {
        return (X) body;
    }
    public void setBody(T body) {
        this.body = body;
    }
    public AttachedBean getAttached() {
        return attached;
    }
    public void setAttached(AttachedBean attached) {
        this.attached = attached;
    }

    @Override
    public String toString() {
        return "ReqObj{" +
                "head=" + head +
                ", body=" + body +
                ", attached=" + attached +
                '}';
    }
}