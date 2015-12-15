package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

public class PageParcel implements Parcelable {
    private final Page page;

    public static PageParcel wrap(Page page) {
        return new PageParcel(page);
    }

    public Page getContents() {
        return page;
    }

    private PageParcel(Page page) {
        this.page = page;
    }

    protected PageParcel(Parcel in) {
        page = new Page(in.readInt());
    }

    public static final Creator<PageParcel> CREATOR = new Creator<PageParcel>() {
        @Override
        public PageParcel createFromParcel(Parcel in) {
            return new PageParcel(in);
        }

        @Override
        public PageParcel[] newArray(int size) {
            return new PageParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page.component1());
    }
}
