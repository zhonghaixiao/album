package com.example.album.dao;

import org.n3r.eql.eqler.annotations.EqlerConfig;
import org.n3r.eql.eqler.annotations.Sql;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@EqlerConfig
@Repository
public interface ActVoteDao {

    @Sql("select * from activity")
    List<Map> getActivities();

}
