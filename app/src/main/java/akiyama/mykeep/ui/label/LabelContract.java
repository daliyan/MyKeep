package akiyama.mykeep.ui.label;

import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelModel;

import java.util.List;

import akiyama.mykeep.BasePresenter;
import akiyama.mykeep.BaseView;

/**
 * Created by aspen on 16/7/9.
 */
public interface LabelContract {

    interface LabelView extends BaseView<LabelPresenter>{
        void loadLabelUi(List<? extends BaseModel> models);
        void refreshUi(boolean isAddSuccess);
    }

    interface LabelPresenter extends BasePresenter{
        void queryLabel();
        void addLabel(LabelModel labelModel);
        void deleteLabel(LabelModel labelModel);
    }
}
