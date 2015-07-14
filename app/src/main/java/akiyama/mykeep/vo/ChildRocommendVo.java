package akiyama.mykeep.vo;

/**
 * 记事的子项目
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  17:39
 */
public class ChildRocommendVo {

    public String title;
    public String subtitle;
    public int image;
    public String updateTime;
    public ChildRocommendVo(String title, String subtitle, String updateTime, int image){
        this.title=title;
        this.subtitle=subtitle;
        this.updateTime=updateTime;
        this.image=image;
    }


}
