package akiyama.mykeep.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import akiyama.mykeep.util.LogUtil;

/**
 * 记事对象
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-24  11:25
 */
@AVClassName("Record")
public class Record extends AVObject{

    public final static String NORMAL="normal";
    public final static String IMPOTANT="important";
    public final static String MORE_IMPORTANT="more_important";
    public Record(){
        super();
    }
    public String getTitle(){
        return getString("title");
    }

    public void setTitle(String title){
        put("title",title);
    }

    public String getContent(){
        return getString("content");
    }

    public void setContent(String content){
        put("content",content);
    }

    public String setLevel(){
        return getString("level");
    }

    public void setLevel(String level){
        put("level",level);
    }

    public String getDateTime(){
        return getString("dateTime");
    }

    public void setDateTime(String dateTime){
        put("dateTime",dateTime);
    }

    public void setCreator(AVUser user) {
        put("creator", user);
    }
    public AVUser getCreator() {
        return getAVUser("creator");
    }

    public void fetchCreator() {
        AVUser usr = getAVUser("creator");
        if (null == usr.getCreatedAt()) {
            try {
                usr.fetchInBackground(null);
            } catch (Exception ex) {
                LogUtil.e("CMT", "failed to fetch user info. cause:" + ex.getMessage());
            }
        }
    }
}
