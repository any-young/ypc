package com.config.util;

import com.config.parser.ConsumerServiceParser;
import com.config.parser.ProviderServiceParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
public class YpcNameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("provider", new ProviderServiceParser());
        registerBeanDefinitionParser("consumer", new ConsumerServiceParser());
    }
}
