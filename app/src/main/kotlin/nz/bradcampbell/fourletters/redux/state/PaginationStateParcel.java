package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PaginationStateParcel implements Parcelable {
    private final PaginationState paginationState;

    public static PaginationStateParcel wrap(PaginationState paginationState) {
        return new PaginationStateParcel(paginationState);
    }

    private PaginationStateParcel(PaginationState paginationState) {
        this.paginationState = paginationState;
    }

    protected PaginationStateParcel(Parcel in) {
        PageParcel currentPage = in.readParcelable(getClass().getClassLoader());
        List<PageParcel> wrappedPages = new ArrayList<>();
        in.readList(wrappedPages, getClass().getClassLoader());
        List<Page> pages = new ArrayList<>(wrappedPages.size());
        for (PageParcel pp : wrappedPages) {
            pages.add(pp.getContents());
        }
        paginationState = new PaginationState(currentPage.getContents(), pages);
    }

    public PaginationState getContents() {
        return paginationState;
    }

    public static final Creator<PaginationStateParcel> CREATOR = new Creator<PaginationStateParcel>() {
        @Override
        public PaginationStateParcel createFromParcel(Parcel in) {
            return new PaginationStateParcel(in);
        }

        @Override
        public PaginationStateParcel[] newArray(int size) {
            return new PaginationStateParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(PageParcel.wrap(paginationState.component1()), 0);
        List<Page> unwrapped = paginationState.component2();
        List<PageParcel> wrapped = new ArrayList<>(unwrapped.size());
        for (Page p : unwrapped) {
            wrapped.add(PageParcel.wrap(p));
        }
        dest.writeList(wrapped);
    }
}
