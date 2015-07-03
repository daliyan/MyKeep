package akiyama.mykeep.bean.vo;

/**
 * 记事的子项目
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  17:39
 */
public class ChildRocommend {

    public String title;
    public String subtitle;
    public int image;

    public ChildRocommend(String title, String subtitle, int image){
        this.title=title;
        this.subtitle=subtitle;
        this.image=image;
    }


}
