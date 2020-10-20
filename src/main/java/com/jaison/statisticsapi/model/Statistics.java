package com.jaison.statisticsapi.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Statistics {

    private BigDecimal sum = BigDecimal.valueOf(0);
    private BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
    private BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
    private BigDecimal avg = BigDecimal.valueOf(0);
    private long count = 0L;

    public Statistics() {
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistics)) return false;
        Statistics resume = (Statistics) o;
        return count == resume.count &&
                Objects.equals(sum, resume.sum) &&
                Objects.equals(max, resume.max) &&
                Objects.equals(min, resume.min) &&
                Objects.equals(avg, resume.avg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, count, max, min, avg);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Statistics.class.getSimpleName() + "[", "]")
                .add("sum=" + sum)
                .add("count=" + count)
                .add("max=" + max)
                .add("min=" + min)
                .add("avg=" + avg)
                .toString();
    }
}
