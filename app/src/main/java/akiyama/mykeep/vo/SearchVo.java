package akiyama.mykeep.vo;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  16:24
 */
public class SearchVo {
    private String name;
    private boolean isCheck;

    public SearchVo(String name,boolean isCheck){
        this.name=name;
        this.isCheck=isCheck;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean getIsCheck() {
        return isCheck;
    }
}
