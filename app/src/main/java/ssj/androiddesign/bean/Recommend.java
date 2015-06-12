package ssj.androiddesign.bean;

/**
 * 用于主页的推荐列表的适配数据
 * @author zhiwu_yan
 * @since 2015年06月11日
 */
public class Recommend {

    public String title;
    public String subtitle;
    public int  image;

    public Recommend(String title, String subtitle, int image){
        this.title=title;
        this.subtitle=subtitle;
        this.image=image;
    }
}
