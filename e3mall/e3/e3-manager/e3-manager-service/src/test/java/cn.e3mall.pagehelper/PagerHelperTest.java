package cn.e3mall.pagehelper;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class PagerHelperTest {
    @Test
    public void testPagehelper() throws Exception{
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper itemMapper=applicationContext.getBean(TbItemMapper.class);

        PageHelper.startPage(1,10);
        TbItemExample tbItemExample=new TbItemExample();

        List<TbItem> list= itemMapper.selectByExample(tbItemExample);
        PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
        System.out.println(pageInfo.getSize());

    }
}
