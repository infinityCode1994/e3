package cn.e3mall.service.impl;

import cn.e3mall.service.ItemService;
import mapper.TbItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.TbItem;
import pojo.TbItemExample;

import java.util.List;

@Service
public class ItemServiceImpl  implements ItemService{

    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public TbItem getItemById(long itemId) {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list !=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
