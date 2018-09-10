package top.vetoer.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import top.vetoer.domain.Timeline;

import java.util.List;

public interface TimeLineRepository extends JpaRepository<Timeline,Long> {
    List<Timeline> findByContentLike(String key, Sort sort);

    Page<Timeline> findAllByUserId(long userId, Pageable pageable);
}
