package com.example.hylaas2server;

import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.session.AS2ComponentNotFoundException;
import com.helger.as2servlet.AS2ReceiveXServletHandlerConstantSession;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.mdn.AS2MDNReceiveXServletHandlerConstantSession;
import com.helger.as2servlet.util.AS2ServletXMLSession;
import com.helger.commons.exception.InitializationException;
import com.helger.commons.http.EHttpMethod;
import com.helger.web.scope.mgr.WebScopeManager;
import com.helger.xservlet.AbstractXServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileNotFoundException;

@Configuration
public class ServletConfig {
    private static final AS2ServletXMLSession AS2_SESSION;

    static {
        try {
            File config = ResourceUtils.getFile("classpath:config.xml");
            AS2_SESSION = new AS2ServletXMLSession(config);
            // Start them once
            AS2_SESSION.getMessageProcessor().startActiveModules();
        } catch (final AS2Exception ex) {
            throw new InitializationException(ex);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Special AS2 receive servlet, that use the global AS2 Session.
     *
     * @author Philip Helger
     */
    public static class MyAS2ReceiveServlet extends AbstractXServlet {
        @Override
        @OverridingMethodsMustInvokeSuper
        public void init() throws ServletException {
            // Multipart is handled specifically inside
            settings().setMultipartEnabled(false);
            handlerRegistry().registerHandler(EHttpMethod.POST, new AS2ReceiveXServletHandlerConstantSession(AS2_SESSION), false);
        }
    }

    /**
     * Special AS2 MDN receive servlet, that use the global AS2 Session.
     *
     * @author Philip Helger
     */
    public static class MyAS2MDNReceiveServlet extends AbstractXServlet {
        @Override
        @OverridingMethodsMustInvokeSuper
        public void init() throws ServletException {
            // Multipart is handled specifically inside
            settings().setMultipartEnabled(false);
            handlerRegistry().registerHandler(EHttpMethod.POST, new AS2MDNReceiveXServletHandlerConstantSession(AS2_SESSION), false);
        }
    }

    /**
     * The ServletContext to be used
     */
    @Autowired
    private ServletContext m_aSC;

    private void _initScope() {
        // Required to be called before the servlet is initialized
        if (!WebScopeManager.isGlobalScopePresent()) {
            AS2WebAppListener.staticInit(m_aSC);
        }
    }

    @Bean
    public ServletRegistrationBean<MyAS2ReceiveServlet> servletRegistrationBeanAS2() {
        _initScope();

        return new ServletRegistrationBean<>(new MyAS2ReceiveServlet(), "/as2");
    }


    /**
     * Call this method at the application end, to shutdown all active modules.
     */
    public static void shutDown() {
        if (AS2_SESSION != null)
            try {
                AS2_SESSION.getMessageProcessor().stopActiveModules();
            } catch (final AS2ComponentNotFoundException ex) {
                // we don't care on shut down
            }
    }
}
