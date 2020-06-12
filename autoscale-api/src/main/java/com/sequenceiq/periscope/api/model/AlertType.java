package com.sequenceiq.periscope.api.model;

public enum AlertType {
    METRIC, TIME, PROMETHEUS, LOAD;

    @Override
    public String toString() {
        switch (this) {
            case TIME: return "SCHEDULE";
            case LOAD: return "LOAD";
            case METRIC: return "METRIC";
            case PROMETHEUS: return "PROMETHEUS";
            default: throw new IllegalArgumentException();
        }
    }
}
