package util;
import android.app.Application;
public class DocumentApi extends Application{
    private String userId;

    private static DocumentApi instance;
    public static DocumentApi getInstance(){
        if(instance==null){
            instance=new DocumentApi();
        }
        return instance;
    }
    public DocumentApi(){}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
