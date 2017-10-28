package edu.usfca.cs.mr.hottest_temperature;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class GeohashTimestampWritable implements WritableComparable<GeohashTimestampWritable> {

    private final Text geohash;
    private final Text timestamp;

    public GeohashTimestampWritable(String geohash, String timestamp) {
        this.geohash = new Text(geohash);
        this.timestamp = new Text(timestamp);
    }

    public GeohashTimestampWritable(Text geohash, Text timestamp) {
        this.geohash = geohash;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(GeohashTimestampWritable o) {
        if (this.geohash.equals(o.geohash)) {
            return this.timestamp.compareTo(o.timestamp);
        } else {
            return this.geohash.compareTo(o.geohash);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeohashTimestampWritable that = (GeohashTimestampWritable) o;
        return Objects.equals(geohash, that.geohash) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geohash, timestamp);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        geohash.write(dataOutput);
        timestamp.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        geohash.readFields(dataInput);
        timestamp.readFields(dataInput);
    }

    @Override
    public String toString() {
        return geohash.toString() + ":" + timestamp.toString();
    }
}
