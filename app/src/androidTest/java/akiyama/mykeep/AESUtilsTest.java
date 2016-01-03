package akiyama.mykeep;

import android.test.ApplicationTestCase;

import akiyama.mykeep.util.AESUtils;

/**
 * Created by Administrator on 2016/1/3.
 */
public class AESUtilsTest extends ApplicationTestCase<AppContext> {
    public AESUtilsTest(Class<AppContext> applicationClass) {
        super(applicationClass);
    }


    public void testAes(){
        try {
            assertEquals(AESUtils.decryptDefalutKey(AESUtils.encryptDefalutKey("1234")),"1234");
            assertEquals(AESUtils.decryptDefalutKey(AESUtils.encryptDefalutKey("1%%%!!234")),"1%%%!!234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
