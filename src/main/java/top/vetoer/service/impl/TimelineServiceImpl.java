package top.vetoer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.vetoer.domain.Timeline;
import top.vetoer.repository.TimeLineRepository;
import top.vetoer.service.TimelineService;

import java.util.List;

@Service("timelineService")
public class TimelineServiceImpl implements TimelineService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TimeLineRepository timeLineRepository;

    @Override
    public List<Timeline> queryCont(String key) {
        logger.debug("查找内容为: "+key);
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        return timeLineRepository.findByContentLike("%"+key+"%",sort);
    }
}
