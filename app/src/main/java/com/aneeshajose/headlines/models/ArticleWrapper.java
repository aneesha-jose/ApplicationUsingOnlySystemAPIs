package com.aneeshajose.headlines.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Aneesha Jose on 2020-04-07.
 */
public class ArticleWrapper implements Parcelable {

    private String status;
    private List<Article> articles;

    public ArticleWrapper() {
    }

    protected ArticleWrapper(Parcel in) {
        status = in.readString();
        articles = in.createTypedArrayList(Article.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeTypedList(articles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArticleWrapper> CREATOR = new Creator<ArticleWrapper>() {
        @Override
        public ArticleWrapper createFromParcel(Parcel in) {
            return new ArticleWrapper(in);
        }

        @Override
        public ArticleWrapper[] newArray(int size) {
            return new ArticleWrapper[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
