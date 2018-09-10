package top.vetoer.service;

import top.vetoer.domain.Timeline;

import java.util.List;

public interface TimelineService {
    List<Timeline> queryCont(String key);
}
