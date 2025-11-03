public class TooSmallText extends Exception {
    private int count;

    public TooSmallText(int count) {
        super("Only found " + count + " words.");
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
