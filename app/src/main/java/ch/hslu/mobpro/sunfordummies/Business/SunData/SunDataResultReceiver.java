package ch.hslu.mobpro.sunfordummies.Business.SunData;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import ch.hslu.mobpro.sunfordummies.Utils.SunDataDTO;

public class SunDataResultReceiver extends ResultReceiver {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_ERROR = 1;
    public static final String PARAM_DATA = "SunData";
    public static final String PARAM_EXCEPTION = "Exception";

    private ResultReceiverCallBack<SunDataDTO> callBack;

    public SunDataResultReceiver(ResultReceiverCallBack<SunDataDTO> callBack) {
        super(new Handler());
        this.callBack = callBack;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode == RESULT_CODE_OK){
            callBack.onSuccess((SunDataDTO)resultData.getSerializable(PARAM_DATA));
        } else {
            callBack.onError((Exception) resultData.getSerializable(PARAM_EXCEPTION));
        }
    }
}
