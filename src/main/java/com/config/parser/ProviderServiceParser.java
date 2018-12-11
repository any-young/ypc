package com.config.parser;

import com.config.util.FrameworkParserUtil;
import com.service.server.SimpleServiceFindHandler;
import com.zk.ProviderBootStrap;
import com.zk.YpcZkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.config.parser.AttributeEnum.*;


/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/7
 */
@Slf4j
public class ProviderServiceParser extends AbstractSingleBeanDefinitionParser {
    private static final String FIND_SERVICE_OBJECT = "serviceFindHandler";
    private static final String ZK_SERVER = "ypc-provider-zkServer";
    private HashSet<String> providers = new HashSet<>();
    public static ConcurrentHashMap<String, String> timeouts = new ConcurrentHashMap<>();

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProviderBootStrap.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        //解析serviceFindHandler到内存中,主要是用来找到内存中的服务类对象
        FrameworkParserUtil.parse(FIND_SERVICE_OBJECT, SimpleServiceFindHandler.class, element, parserContext);
        FrameworkParserUtil.parse(ZK_SERVER, YpcZkServer.class, element, parserContext, beanDefinition -> {
            beanDefinition.getPropertyValues().addPropertyValue(ZK_ADDRESS.value(), element.getAttribute(ZK_ADDRESS.value()));
        });
        NodeList nodeList = element.getElementsByTagName("ypc:provideServices");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element el = (Element) nodeList.item(i);
            String className = el.getAttribute("className");
            String timeout = el.getAttribute("timeout");
            providers.add(className);
            timeouts.putIfAbsent(loadClassName(className), timeout);
        }
        builder.addPropertyValue(PROXY.value(), element.getAttribute(PROXY.value()));
        builder.addPropertyValue(PORT.value(), element.getAttribute(PORT.value()));
        builder.addPropertyValue("zkServer", new RuntimeBeanReference(ZK_SERVER));
        builder.addPropertyValue("providers", providers);
    }

    /**
     * 获取接口的名称
     * 这里即为无论配置的是接口还是实现类，保存的都是接口的className
     * @param className
     * @return
     */
    private String loadClassName(String className){
        Class<?> loadClass;
        try {
            loadClass = this.getClass().getClassLoader().loadClass(className);
            if(!loadClass.isInterface()){
                if (loadClass.getInterfaces()!=null && loadClass.getInterfaces().length>0)
                return loadClass.getInterfaces()[0].getName();
            }
        } catch (ClassNotFoundException e) {
            log.error(this.getClass().getName()+" not load service {}" ,className);
        }
        return className;
    }
}
