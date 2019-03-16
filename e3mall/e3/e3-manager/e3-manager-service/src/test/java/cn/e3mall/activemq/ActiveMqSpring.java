package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class ActiveMqSpring {
    @Test
    public void testSpringActiveMq() throws Exception {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从spring容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从spring容器中取Destination对象
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        //使用JmsTemplate对象发送消息。
        jmsTemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建一个消息对象并返回
                TextMessage textMessage = session.createTextMessage("spring activemq queue message");
                return textMessage;
            }
        });
    }
    @Test
    public void testQueueProducer() throws Exception {
        // 第一步：初始化一个spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        // 第二步：从容器中获得JMSTemplate对象。
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        // 第三步：从容器中获得一个Destination对象
        Queue queue = (Queue) applicationContext.getBean("queueDestination");
        // 第四步：使用JMSTemplate对象发送消息，需要知道Destination
        jmsTemplate.send(queue, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring activemq test");
                return textMessage;
            }
        });
    }

}
