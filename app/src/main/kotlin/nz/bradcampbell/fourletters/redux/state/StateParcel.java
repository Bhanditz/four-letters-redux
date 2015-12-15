package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

public class StateParcel implements Parcelable {
    private final State state;

    public static StateParcel wrap(State state) {
        return new StateParcel(state);
    }

    private StateParcel(State state) {
        this.state = state;
    }

    protected StateParcel(Parcel in) {
        PaginationStateParcel c1 = in.readParcelable(getClass().getClassLoader());
        GameStateParcel c2 = in.readParcelable(getClass().getClassLoader());
        MenuStateParcel c3 = in.readParcelable(getClass().getClassLoader());
        this.state = new State(c1.getContents(), c2.getContents(), c3.getContents());
    }

    public State getContents() {
        return state;
    }

    public static final Creator<StateParcel> CREATOR = new Creator<StateParcel>() {
        @Override
        public StateParcel createFromParcel(Parcel in) {
            return new StateParcel(in);
        }

        @Override
        public StateParcel[] newArray(int size) {
            return new StateParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(PaginationStateParcel.wrap(state.component1()), 0);
        dest.writeParcelable(GameStateParcel.wrap(state.component2()), 0);
        dest.writeParcelable(MenuStateParcel.wrap(state.component3()), 0);
    }
}
