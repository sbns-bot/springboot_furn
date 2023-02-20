package furn;

import furn.bean.Furn;
import furn.mapper.FurnMapper;
import furn.service.FurnService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.annotation.Target;
import java.util.List;

@SpringBootTest
public class ApplicationTest {
    @Resource
    private FurnMapper furnMapper;

    @Resource
    private FurnService furnService;

    @Test
    public void test1() {
        Furn furn = furnMapper.selectById(1);
        System.out.println("furn=" + furn);
    }

    @Test
    public void test2() {
        List<Furn> list = furnService.list();
        for (Furn furn : list
        ) {
            System.out.println("furn=" + furn);
        }
    }
}
