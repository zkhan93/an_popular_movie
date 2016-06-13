package io.github.zkhan93.pm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zeeshan Khan on 6/13/2016.
 */
public class Review implements Parcelable {
    private String id;
    private String author, content, url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Review() {
    }

    private Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
    public static final Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }

        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

    };
}
