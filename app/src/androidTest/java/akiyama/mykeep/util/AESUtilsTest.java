package akiyama.mykeep.util;

import android.test.InstrumentationTestCase;

import com.akiyama.base.utils.AESUtils;

import akiyama.mykeep.AppContext;

/**
 * Created by Administrator on 2016/1/3.
 */
public class AESUtilsTest extends InstrumentationTestCase {

    public void testAes(){
        try {
            assertEquals(AESUtils.decryptDefalutKey(AESUtils.encryptDefalutKey("1234")),"1234");
            assertEquals(AESUtils.decryptDefalutKey(AESUtils.encryptDefalutKey("1%%%!!234")),"1%%%!!234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
