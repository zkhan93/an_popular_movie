package io.github.zkhan93.pm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Zeeshan Khan on 6/12/2016.
 */
public class Trailer implements Parcelable {
    private String id;
    private String name, key;
    public Trailer(){}
    private Trailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        key = in.readString();
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(key);
    }
    public static final Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }

        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

    };
}
