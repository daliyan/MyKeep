package akiyama.mykeep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  11:06
 */
public class RecordEditTextView extends EditText{

    private static final String TAG="RecordEditTextView";
    private Context mContext;
    private static final String LIST_MODE="list_mode";//列表模式
    private static final String COMMON_MODE="common_mode";//普通模式
    public RecordEditTextView(Context context){
        super(context);
    }

    public RecordEditTextView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public RecordEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

   private void init(Context context){
       this.mContext = context;

   }
}
