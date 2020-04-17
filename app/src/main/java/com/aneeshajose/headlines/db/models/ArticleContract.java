package com.aneeshajose.headlines.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.aneeshajose.headlines.models.Article;
import com.aneeshajose.headlines.utils.OptionalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public final class ArticleContract implements BaseContract<Article> {

    private static ArticleContract INSTANCE = null;

    /* Inner class that defines the table contents */
    public static class Article implements BaseColumns {
        public static final String TABLE_NAME = "article";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_HEADLINE = "headline";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE = "url_to_image";
        public static final String COLUMN_NAME_PUBLISHED_AT = "published_at";
        public static final String COLUMN_NAME_SOURCE = "source";
    }

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ArticleContract() {
    }

    public static ArticleContract getInstance() {
        if (INSTANCE == null) INSTANCE = new ArticleContract();
        return INSTANCE;
    }

    //Every contract must define its create table statement
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Article.TABLE_NAME + " (" +
                    Article._ID + " INTEGER PRIMARY KEY," +
                    Article.COLUMN_NAME_AUTHOR + " TEXT," +
                    Article.COLUMN_NAME_HEADLINE + " TEXT," +
                    Article.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    Article.COLUMN_NAME_URL + " TEXT," +
                    Article.COLUMN_NAME_URL_TO_IMAGE + " TEXT," +
                    Article.COLUMN_NAME_PUBLISHED_AT + " TEXT," +
                    Article.COLUMN_NAME_SOURCE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Article.TABLE_NAME;

    @Override
    public void insert(com.aneeshajose.headlines.models.Article element, SQLiteDatabase db) {
        db.insert(Article.TABLE_NAME, "", getContentValue(element));
    }

    @Override
    public void update(com.aneeshajose.headlines.models.Article element, SQLiteDatabase db) {
        // Define 'where' part of query.
        String selection = Article.COLUMN_NAME_URL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {element.getUrl()};
        // Issue SQL statement.
        int deletedRows = db.update(Article.TABLE_NAME, getContentValue(element), selection, selectionArgs);
    }

    private ContentValues getContentValue(com.aneeshajose.headlines.models.Article article) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Article.COLUMN_NAME_AUTHOR, OptionalUtils.orEmpty(article.getAuthor()));
        values.put(Article.COLUMN_NAME_DESCRIPTION, OptionalUtils.orEmpty(article.getDescription()));
        values.put(Article.COLUMN_NAME_HEADLINE, OptionalUtils.orEmpty(article.getTitle()));
        values.put(Article.COLUMN_NAME_PUBLISHED_AT, OptionalUtils.orEmpty(article.getPublishedAt()));
        values.put(Article.COLUMN_NAME_URL, OptionalUtils.orEmpty(article.getUrl()));
        values.put(Article.COLUMN_NAME_URL_TO_IMAGE, OptionalUtils.orEmpty(article.getUrlToImage()));
        values.put(Article.COLUMN_NAME_SOURCE, article.getSource() == null ? "" : OptionalUtils.orEmpty(article.getSource().getName()));
        return values;
    }

    @Override
    public void delete(com.aneeshajose.headlines.models.Article element, SQLiteDatabase db) {
        // Define 'where' part of query.
        String selection = Article.COLUMN_NAME_URL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {element.getUrl()};
        // Issue SQL statement.
        db.delete(Article.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public Cursor readAllEntries(SQLiteDatabase db) {
        String[] projection = {Article._ID,
                Article.COLUMN_NAME_AUTHOR,
                Article.COLUMN_NAME_HEADLINE,
                Article.COLUMN_NAME_DESCRIPTION,
                Article.COLUMN_NAME_URL,
                Article.COLUMN_NAME_URL_TO_IMAGE,
                Article.COLUMN_NAME_PUBLISHED_AT,
                Article.COLUMN_NAME_SOURCE};

        return db.query(
                Article.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
    }

    public List<com.aneeshajose.headlines.models.Article> getArticles(Cursor cursor) {
        List<com.aneeshajose.headlines.models.Article> articles = new ArrayList<>();
        Map<String, Integer> indices = getColumnIndices(cursor);
        if (cursor.moveToFirst()) {
            do {
                articles.add(getArticle(cursor, indices));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public com.aneeshajose.headlines.models.Article getArticle(Cursor cursor) {
        Map<String, Integer> indices = getColumnIndices(cursor);
        return getArticle(cursor, indices);
    }

    private Map<String, Integer> getColumnIndices(Cursor cursor) {
        Map<String, Integer> indices = new HashMap<>();
        indices.put(Article.COLUMN_NAME_AUTHOR, cursor.getColumnIndex(Article.COLUMN_NAME_AUTHOR));
        indices.put(Article.COLUMN_NAME_DESCRIPTION, cursor.getColumnIndex(Article.COLUMN_NAME_DESCRIPTION));
        indices.put(Article.COLUMN_NAME_HEADLINE, cursor.getColumnIndex(Article.COLUMN_NAME_HEADLINE));
        indices.put(Article.COLUMN_NAME_PUBLISHED_AT, cursor.getColumnIndex(Article.COLUMN_NAME_PUBLISHED_AT));
        indices.put(Article.COLUMN_NAME_URL, cursor.getColumnIndex(Article.COLUMN_NAME_URL));
        indices.put(Article.COLUMN_NAME_URL_TO_IMAGE, cursor.getColumnIndex(Article.COLUMN_NAME_URL_TO_IMAGE));
        indices.put(Article.COLUMN_NAME_SOURCE, cursor.getColumnIndex(Article.COLUMN_NAME_SOURCE));
        return indices;
    }

    private com.aneeshajose.headlines.models.Article getArticle(Cursor cursor, Map<String, Integer> columnIndices) {
        com.aneeshajose.headlines.models.Article article = new com.aneeshajose.headlines.models.Article();
        article.setSaved(true);
        article.setAuthor(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_AUTHOR)));
        article.setDescription(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_AUTHOR)));
        article.setTitle(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_HEADLINE)));
        article.setPublishedAt(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_PUBLISHED_AT)));
        article.setUrl(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_URL)));
        article.setUrlToImage(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_URL_TO_IMAGE)));
        article.setSource(OptionalUtils.getStringFromCursor(cursor, columnIndices.get(Article.COLUMN_NAME_SOURCE)));
        return article;
    }
}
