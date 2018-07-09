package com.superc.bannerdairy.model;

/**
 * Created by Amorr on 2017/11/20.
 * //首页传播知识实体类
 */

public class KnowledgeItem {

    /**
     * article_id : 21
     * article_category : 2
     * article_title : lallll
     * article_content : asdeghtjmnbv
     * article_image : null
     * display : 1
     * creator : 0
     * created : 2017-11-16 18:31:46
     * modifier : 0
     * modified : 2017-11-22 00:27:12
     * delete_time : null
     * category : {"article_category_id":"2","article_category_name":"客户01","article_category_type":"2","article_category_ico":null,"creator":null,"created":"1970-01-01 08:00:00","modifier":"0","modified":"2017-11-22 00:32:08","delete_time":null}
     * comment : null
     */

    public String article_id;
    public String article_category;
    public String article_title;
    public String article_content;
    public Object article_image;
    public String display;
    public String creator;
    public String created;
    public String modifier;
    public String modified;
    public Object delete_time;
    public CategoryBean category;
    public Object comment;
    public String pageview;//阅读数量

    public KnowledgeItem(String article_id, String article_category, String article_title, String article_content, Object article_image, String display, String creator, String created, String modifier, String modified, Object delete_time, CategoryBean category, Object comment,String pageView) {
        this.pageview=pageView;
        this.article_id = article_id;
        this.article_category = article_category;
        this.article_title = article_title;
        this.article_content = article_content;
        this.article_image = article_image;
        this.display = display;
        this.creator = creator;
        this.created = created;
        this.modifier = modifier;
        this.modified = modified;
        this.delete_time = delete_time;
        this.category = category;
        this.comment = comment;
    }

    public KnowledgeItem() {
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_category() {
        return article_category;
    }

    public void setArticle_category(String article_category) {
        this.article_category = article_category;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public String getPageView() {
        return pageview;
    }

    public void setPageView(String pageView) {
        this.pageview = pageView;
    }

    public Object getArticle_image() {
        return article_image;
    }

    public void setArticle_image(Object article_image) {
        this.article_image = article_image;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Object getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Object delete_time) {
        this.delete_time = delete_time;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public static class CategoryBean {
        /**
         * article_category_id : 2
         * article_category_name : 客户01
         * article_category_type : 2
         * article_category_ico : null
         * creator : null
         * created : 1970-01-01 08:00:00
         * modifier : 0
         * modified : 2017-11-22 00:32:08
         * delete_time : null
         */

        public String article_category_id;
        public String article_category_name;
        public String article_category_type;
        public Object article_category_ico;
        public Object creator;
        public String created;
        public String modifier;
        public String modified;
        public Object delete_time;

        public String getArticle_category_id() {
            return article_category_id;
        }

        public void setArticle_category_id(String article_category_id) {
            this.article_category_id = article_category_id;
        }

        public String getArticle_category_name() {
            return article_category_name;
        }

        public void setArticle_category_name(String article_category_name) {
            this.article_category_name = article_category_name;
        }

        public String getArticle_category_type() {
            return article_category_type;
        }

        public void setArticle_category_type(String article_category_type) {
            this.article_category_type = article_category_type;
        }

        public Object getArticle_category_ico() {
            return article_category_ico;
        }

        public void setArticle_category_ico(Object article_category_ico) {
            this.article_category_ico = article_category_ico;
        }

        public Object getCreator() {
            return creator;
        }

        public void setCreator(Object creator) {
            this.creator = creator;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }
    }
}
