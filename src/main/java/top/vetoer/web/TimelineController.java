package top.vetoer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import top.vetoer.comm.Const;
import top.vetoer.comm.aop.LoggerManage;
import top.vetoer.domain.Timeline;
import top.vetoer.domain.User;
import top.vetoer.domain.result.ExceptionMsg;
import top.vetoer.domain.result.Response;
import top.vetoer.domain.result.ResponseData;
import top.vetoer.repository.TimeLineRepository;
import top.vetoer.service.TimelineService;
import top.vetoer.utils.DateUtils;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/timeline")
public class TimelineController extends BaseController{

    @Autowired
    private TimeLineRepository timeLineRepository;

    @Resource
    private TimelineService timelineService;

    @PostMapping(value = "/add")
    @LoggerManage(description = "add timeline")
    public Response addContent(Timeline timeline){
        try {
            if(null==timeline || "".equals(timeline.getContent())){
                return result(ExceptionMsg.TimeLineContentEmpty);
            }
            if(getSession().getAttribute(Const.LOGIN_SESSION_KEY)==null){
                return result(ExceptionMsg.NeedLogin);
            }
            User user = super.getUser();
            timeline.setCreateTime(DateUtils.getCurrentTime());
            timeline.setUserId(user.getId());
            timeLineRepository.save(timeline);
            return result();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result(ExceptionMsg.FAILED);
    }

    @PostMapping(value = "/load")
    @LoggerManage(description = "首页流加载数据")
    public ResponseData steamLoad(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "15") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Timeline> pages = timeLineRepository.findAll(pageable);
        return new ResponseData(pages);
    }

    @GetMapping(value = "/query")
    @LoggerManage(description = "查找key")
    public ResponseData query(@RequestParam(value = "key") String key){
        if(key!=null && !"".equals(key.trim())){
            List<Timeline> datas = timelineService.queryCont(key);
            return new ResponseData(datas);
        }else{
            return new ResponseData(ExceptionMsg.ParamError);
        }
    }

}
