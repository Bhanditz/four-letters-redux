package nz.bradcampbell.fourletters.redux.state;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GameStateParcel implements Parcelable {
    private final GameState gameState;

    public static GameStateParcel wrap(GameState gameState) {
        return new GameStateParcel(gameState);
    }

    private GameStateParcel(GameState gameState) {
        this.gameState = gameState;
    }

    protected GameStateParcel(Parcel in) {
        GameState result = null;
        if (in.readByte() != 0) {
            List<LetterParcel> wrapped = new ArrayList<>();
            in.readList(wrapped, getClass().getClassLoader());
            List<Letter> unwrapped = new ArrayList<>(wrapped.size());
            for (LetterParcel lp : wrapped) {
                unwrapped.add(lp.getContents());
            }
            Letter c2 = ((LetterParcel) in.readParcelable(getClass().getClassLoader())).getContents();
            Letter c3 = ((LetterParcel) in.readParcelable(getClass().getClassLoader())).getContents();
            Letter c4 = ((LetterParcel) in.readParcelable(getClass().getClassLoader())).getContents();
            Letter c5 = ((LetterParcel) in.readParcelable(getClass().getClassLoader())).getContents();
            List<String> possibleAnswers = new ArrayList<>();
            in.readList(possibleAnswers, getClass().getClassLoader());
            result = new GameState(unwrapped,
                    c2, c3, c4, c5,
                    possibleAnswers,
                    in.readInt(),
                    in.readLong());
        }
        gameState = result;
    }

    public GameState getContents() {
        return gameState;
    }

    public static final Creator<GameStateParcel> CREATOR = new Creator<GameStateParcel>() {
        @Override
        public GameStateParcel createFromParcel(Parcel in) {
            return new GameStateParcel(in);
        }

        @Override
        public GameStateParcel[] newArray(int size) {
            return new GameStateParcel[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (gameState != null ? 1 : 0));
        if (gameState != null) {
            List<Letter> unwrapped = gameState.component1();
            List<LetterParcel> wrapped = new ArrayList<>(unwrapped.size());
            for (Letter l : unwrapped) {
                wrapped.add(LetterParcel.wrap(l));
            }
            dest.writeList(wrapped);
            dest.writeParcelable(LetterParcel.wrap(gameState.component2()), 0);
            dest.writeParcelable(LetterParcel.wrap(gameState.component3()), 0);
            dest.writeParcelable(LetterParcel.wrap(gameState.component4()), 0);
            dest.writeParcelable(LetterParcel.wrap(gameState.component5()), 0);
            dest.writeList(gameState.component6());
            dest.writeInt(gameState.component7());
            dest.writeLong(gameState.component8());
        }
    }
}
