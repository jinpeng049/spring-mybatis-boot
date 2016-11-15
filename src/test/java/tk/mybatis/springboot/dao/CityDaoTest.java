package tk.mybatis.springboot.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.springboot.BaseTest;

/**
 * Created by Administrator on 2016/11/15.
 */
public class CityDaoTest extends BaseTest {

    @Autowired
    private CityDao cityDao;
    @Test
    public void select() throws Exception {
        System.err.println(cityDao.select());
    }

}