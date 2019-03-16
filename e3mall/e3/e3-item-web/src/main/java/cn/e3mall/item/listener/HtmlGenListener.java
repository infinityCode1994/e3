package cn.e3mall.item.listener;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HtmlGenListener implements MessageListener {
    @Value("${HTML_GEN_PATN}")
    private String HTML_GEN_PATN;
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Override
    public void onMessage(Message message) {
        try {
            //取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            Thread.sleep(1000);
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getTbitenDescById(itemId);
            Map data = new HashMap<>();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 第四步：加载一个模板，创建一个模板对象。
            Template template = configuration.getTemplate("item.ftl");
            // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
            Writer out = new FileWriter(new File(HTML_GEN_PATN+itemId+".html"));
            // 第七步：调用模板对象的process方法输出文件。
            template.process(data, out);
            // 第八步：关闭流。
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
