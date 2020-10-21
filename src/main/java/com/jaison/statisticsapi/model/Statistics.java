package com.jaison.statisticsapi.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Statistics {

    private BigDecimal sum;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal avg;
    private long count;

    public Statistics() {
    }

    public Statistics(BigDecimal sum, BigDecimal max, BigDecimal min, BigDecimal avg, long count) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.avg = avg;
        this.count = count;
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
