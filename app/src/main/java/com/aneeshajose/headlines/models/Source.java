package com.aneeshajose.headlines.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aneesha Jose on 2020-04-05.
 */
public class Source implements Parcelable {

    private String id;
    private String name;

    protected Source(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public Source() {
    }

    Source(String sourceString) {
        name = sourceString;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
