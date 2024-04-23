package lab5;

public class Tuple {
    private char depFrom;
    private char depTo;

    public Tuple(char depFrom, char depTo) {
        this.depFrom = depFrom;
        this.depTo = depTo;
    }

    public char getDepFrom() {
        return depFrom;
    }

    public void setDepFrom(char depFrom) {
        this.depFrom = depFrom;
    }

    public char getDepTo() {
        return depTo;
    }

    public void setDepTo(char depTo) {
        this.depTo = depTo;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Tuple that = (Tuple) o;

        if (depFrom != that.depFrom)
            return false;
        return depTo == that.depTo;
    }

    @Override public int hashCode() {
        int result = (int) depFrom;
        result = 31 * result + (int) depTo;
        return result;
    }

    @Override
    public String toString() {
        return "(" + depFrom + ',' + depTo + ')';
    }
}