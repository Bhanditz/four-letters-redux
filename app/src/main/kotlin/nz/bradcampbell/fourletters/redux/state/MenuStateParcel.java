package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuStateParcel implements Parcelable {
    private final MenuState menuState;

    public static MenuStateParcel wrap(MenuState menuState) {
        return new MenuStateParcel(menuState);
    }

    private MenuStateParcel(MenuState menuState) {
        this.menuState = menuState;
    }

    protected MenuStateParcel(Parcel in) {
        menuState = new MenuState(in.readByte() != 0);
    }

    public MenuState getContents() {
        return menuState;
    }

    public static final Creator<MenuStateParcel> CREATOR = new Creator<MenuStateParcel>() {
        @Override
        public MenuStateParcel createFromParcel(Parcel in) {
            return new MenuStateParcel(in);
        }

        @Override
        public MenuStateParcel[] newArray(int size) {
            return new MenuStateParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (menuState.component1() ? 1 : 0));
    }
}
