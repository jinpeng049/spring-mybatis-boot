package tk.mybatis.springboot.service;

import org.junit.Test;
import tk.mybatis.springboot.BaseTest;

import javax.annotation.Resource;

/**
 * Created by pengpeng on 2016/11/5.
 */
public class CityServiceTest extends BaseTest {
    @Resource
    private CityService cityService;

    @Test
    public void getAll() {

    }

    @Test
    public void selectByExample() {
        System.err.println(cityService.selectByExample());
    }

    @Test
    public void getById() {

    }

    @Test
    public void deleteById() {

    }

    @Test
    public void save() {

    }

}