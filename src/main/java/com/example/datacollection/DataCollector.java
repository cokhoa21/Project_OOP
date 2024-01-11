package com.example.datacollection;

import java.util.List;

public interface DataCollector<T> {
    List<T> collectData();
}
