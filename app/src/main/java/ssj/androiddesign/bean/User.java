package ssj.androiddesign.bean;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * 帐号子类化
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-24  14:02
 */
public class User extends AVUser{

    public List getRecord() {
        return (List)getList("records");
    }

    public void addRecord(Record record) {
        addUnique("records", record);
    }

}
