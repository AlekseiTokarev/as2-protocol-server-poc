package com.example.hylaas2server;

import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.message.IMessage;
import com.helger.as2servlet.util.AS2ServletReceiverModule;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

@Slf4j
public class LoggingAS2ServletReceiverModule extends AS2ServletReceiverModule {

    @Override
    public void handleError(@Nonnull IMessage aMsg, @Nonnull AS2Exception aSrcEx) {
        log.error("Error occurred", aSrcEx);
        super.handleError(aMsg, aSrcEx);
    }

}
