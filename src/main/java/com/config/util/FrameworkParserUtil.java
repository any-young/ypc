package com.config.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/11/27
 */
public class FrameworkParserUtil {
    private static final Logger log = LoggerFactory.getLogger(FrameworkParserUtil.class);

    public static void parse(String id, Class<?> beanType, Element rootElement, ParserContext parserContext, ComponentParser componentParser) {
        try {
            Object eleSource = parserContext.extractSource(rootElement);
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setSource(eleSource);//bean解析的来源
            beanDefinition.setRole(BeanDefinition.ROLE_APPLICATION);//bean的角色
            beanDefinition.setBeanClass(beanType);//设置class
            beanDefinition.setLazyInit(false);//懒加载
            beanDefinition.setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_NONE);//设置依赖检查
            beanDefinition.setAutowireCandidate(true);//自动注入
            beanDefinition.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);//根据name自动注入
            if (componentParser != null){//如果需要从xml中解析出参数，则将attribute注册到beanDefinition
                componentParser.parseAttribute(beanDefinition);
            }
            if (id == null){//如果没有给出bean的id，则用自动生成的beanName作为id来保存
                id = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, parserContext.getRegistry());
            }
            //注册BeanDefinition
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, id);
            BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, parserContext.getRegistry());
            //在parserContext上面注册BeanComponentDefinition
            BeanComponentDefinition beanComponentDefinition = new BeanComponentDefinition(beanDefinitionHolder);
            parserContext.registerComponent(beanComponentDefinition);
        }catch (Throwable e){
            parserContext.getReaderContext().error(e.getMessage(), rootElement);
        }
    }

    public static void parse(Class<?> beanType, Element rootElement, ParserContext parserContext, ComponentParser componentParser) {
        parse(null, beanType, rootElement, parserContext, componentParser);
    }

    public static void parse(Class<?> beanType, Element rootElement, ParserContext parserContext){
        parse(null, beanType, rootElement, parserContext, null);
    }

    public static void parse(String id, Class<?> beanType, Element rootElement, ParserContext parserContext){
        parse(id, beanType, rootElement, parserContext, null);
    }

    public interface ComponentParser {
        void parseAttribute(RootBeanDefinition beanDefinition);
    }

}
