package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

public class LetterParcel implements Parcelable {
    private final Letter letter;

    public static LetterParcel wrap(Letter letter) {
        return new LetterParcel(letter);
    }

    private LetterParcel(Letter letter) {
        this.letter = letter;
    }

    protected LetterParcel(Parcel in) {
        letter = new Letter((Position) in.readSerializable(), (char) in.readInt());
    }

    public Letter getContents() {
        return letter;
    }

    public static final Creator<LetterParcel> CREATOR = new Creator<LetterParcel>() {
        @Override
        public LetterParcel createFromParcel(Parcel in) {
            return new LetterParcel(in);
        }

        @Override
        public LetterParcel[] newArray(int size) {
            return new LetterParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(letter.component1());
        dest.writeInt(letter.component2());
    }
}
