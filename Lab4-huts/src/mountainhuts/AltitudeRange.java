package mountainhuts;

public class AltitudeRange implements Comparable<AltitudeRange> { 
    public final int minValue;
    public final int maxValue;

    public static final AltitudeRange UNDEFINED = new AltitudeRange(0, Integer.MAX_VALUE);

    public AltitudeRange(int minValue, int maxValue) {
        if (minValue < 0 || maxValue < 0 || minValue > maxValue) {
            throw new IllegalArgumentException("Invalid altitude range");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public boolean isWithin(int altitude) {
        return altitude > minValue && altitude <= maxValue;
    }

    @Override
    public int compareTo(AltitudeRange other) {
        if (this.minValue != other.minValue) {
            return Integer.compare(this.minValue, other.minValue);
        }
        return Integer.compare(this.maxValue, other.maxValue);
    }

    @Override
    public String toString() {
        return minValue + "-" + (Integer.MAX_VALUE == maxValue ? "INF" : maxValue);
    }

    public static AltitudeRange fromString(String range) {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format");
        }
        int min = Integer.parseInt(parts[0]);
        int max = Integer.parseInt(parts[1]);
        return new AltitudeRange(min, max);
    }
}
