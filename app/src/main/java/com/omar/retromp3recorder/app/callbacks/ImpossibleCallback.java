package com.omar.retromp3recorder.app.callbacks;

/**
 * Created by omar on 8/31/15.
 */
public class ImpossibleCallback {

    public interface ICallback{
        void Success(String s);
        void Error(String s);
    }


    String responce;

    public  void getResponce (ICallback callback){
                callback.Success("somevalue");
    }

    public  void run(){
        getResponce(  new ICallback() {
            @Override
            public void Success(String s) {
                responce = s;
            }

            @Override
            public void Error(String s) {
                responce = s;
            }
        });
    }
    public String  getResponceString(){return responce;}
}
