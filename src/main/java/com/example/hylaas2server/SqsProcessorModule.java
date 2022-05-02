package com.example.hylaas2server;

import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.module.AbstractProcessorModule;
import com.helger.as2lib.processor.storage.IProcessorStorageModule;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class SqsProcessorModule extends AbstractProcessorModule implements IProcessorStorageModule {
    private static final AtomicReference<SqsProcessorModule> INSTANCE = new AtomicReference<>();
    @Setter
    private SqsClient sqsClient;

    public SqsProcessorModule() {
        INSTANCE.set(this);
    }

    public static SqsProcessorModule getInstance() {
        return INSTANCE.get();
    }

    @Override
    public boolean canHandle(@Nonnull String sAction, @Nonnull IMessage aMsg, @Nullable Map<String, Object> aOptions) {
        return DO_STORE.equals(sAction);
    }

    @Override
    public void handle(@Nonnull String sAction, @Nonnull IMessage aMsg, @Nullable Map<String, Object> aOptions) throws AS2Exception {
        String message;
        try {
            message = new String(aMsg.getData().getInputStream().readAllBytes());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("publish message {}", message);
        GetQueueUrlResponse queue = sqsClient.getQueueUrl(builder ->
                builder.queueName("as2-queue").build()
        );
        sqsClient.sendMessage(request -> request.queueUrl(queue.queueUrl())
                .messageBody(message));
    }
}
