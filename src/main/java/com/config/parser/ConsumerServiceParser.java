package com.config.parser;

import com.config.util.FrameworkParserUtil;
import com.service.SimpleServiceFindHandler;
import com.zk.ConsumerBootStrap;
import com.zk.YpcZkServer;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.config.parser.AttributeEnum.*;


/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public class ConsumerServiceParser extends AbstractSingleBeanDefinitionParser {

    private static final String ZK_SERVER = "ypc-consumer-zkServer";
    @Override
    protected Class<?> getBeanClass(Element element) {
        return ConsumerBootStrap.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        //解析解析并且初始化
        FrameworkParserUtil.parse(ZK_SERVER, YpcZkServer.class, element, parserContext, beanDefinition -> {
            beanDefinition.getPropertyValues().addPropertyValue(ZK_ADDRESS.value(), element.getAttribute(ZK_ADDRESS.value()));
            beanDefinition.getPropertyValues().addPropertyValue(PROTOCOL.value(),element.getAttribute(PROTOCOL.value()));
        });
        NodeList nodeList = element.getElementsByTagName("ypc:consumeServices");
        List<String> classes = new ArrayList<>();
        for (int i =0 ;i<nodeList.getLength(); i++){
            Element el = (Element) nodeList.item(i);
            String name = el.getAttribute("name");
            String clazz = el.getAttribute("interface");
            classes.add(clazz);
        }
        //将ZK_SERVER的指针赋值给zkServer
        builder.addPropertyValue("classes",classes);
        builder.addPropertyValue("zkServer", new RuntimeBeanReference(ZK_SERVER));
        builder.addPropertyValue(LOAD_BALANCE.value(), element.getAttribute(LOAD_BALANCE.value()));
    }
}
