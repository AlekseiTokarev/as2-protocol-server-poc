package com.example.hylaas2server;

import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2servlet.AS2ReceiveXServletHandlerConstantSession;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.util.AS2ServletXMLSession;
import com.helger.commons.http.EHttpMethod;
import com.helger.web.scope.mgr.WebScopeManager;
import com.helger.xservlet.AbstractXServlet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;

@Configuration
public class As2Config {

    @Bean
    AS2ServletXMLSession as2ServletXMLSession() throws FileNotFoundException, AS2Exception {
        File config = ResourceUtils.getFile("classpath:config.xml");

        AS2ServletXMLSession as2Session = new AS2ServletXMLSession(config);
        as2Session.getMessageProcessor().startActiveModules();
        return as2Session;
    }

    @Bean
    public ServletRegistrationBean<MyAS2ReceiveServlet> servletRegistrationBeanAS2(AS2ServletXMLSession as2Session, ServletContext servletContext) {
        if (!WebScopeManager.isGlobalScopePresent()) {
            AS2WebAppListener.staticInit(servletContext);
        }
        return new ServletRegistrationBean<>(new MyAS2ReceiveServlet(as2Session), "/as2");
    }

}

@RequiredArgsConstructor
class MyAS2ReceiveServlet extends AbstractXServlet {
    private final AS2ServletXMLSession xmlSession;

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init() {
        // Multipart is handled specifically inside
        settings().setMultipartEnabled(false);
        handlerRegistry().registerHandler(EHttpMethod.POST, new AS2ReceiveXServletHandlerConstantSession(xmlSession), false);
    }
}
