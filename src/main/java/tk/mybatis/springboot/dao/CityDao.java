package tk.mybatis.springboot.dao;

import tk.mybatis.springboot.model.City;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public interface CityDao {

    List<City> select();

}
