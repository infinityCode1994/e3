package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if (type==1){
            criteria.andUsernameEqualTo(param);
        }else if (type==2){
            criteria.andPhoneEqualTo(param);
        }else if(type==3){
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(400,"参数出错");
        }
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (list != null && list.size()>0){
            return E3Result.ok(false);
        }else {
            return E3Result.ok();
        }
    }

    @Override
    public E3Result register(TbUser tbUser) {
        if(StringUtils.isEmpty(tbUser.getUsername()) || StringUtils.isEmpty(tbUser.getPassword())
                || StringUtils.isEmpty(tbUser.getPhone())){
            return  E3Result.build(400,"用户数据不完整，注册失败");
        }
        E3Result result_u = checkData(tbUser.getUsername(), 1);
        if (!(boolean)result_u.getData()){
            return  E3Result.build(400,"用户名占用，注册失败");
        }
        E3Result result_p = checkData(tbUser.getPhone(), 2);
        if (!(boolean)result_p.getData()){
            return  E3Result.build(400,"手机号占用，注册失败");
        }
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        String md5pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5pass);
        tbUserMapper.insert(tbUser);
        return E3Result.ok();
    }
}
