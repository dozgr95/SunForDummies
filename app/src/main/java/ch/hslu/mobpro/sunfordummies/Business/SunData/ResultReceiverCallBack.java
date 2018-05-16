package ch.hslu.mobpro.sunfordummies.Business.SunData;

public interface ResultReceiverCallBack<T> {
    void onSuccess(T data);
    void onError(Exception exception);
}
