package org.jeecg.modules.inscription.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 脱敏序列化类
 */
public class DesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeType type;

    private Integer startIndex;

    private Integer endIndex;

    public DesensitizeSerializer() {
    }

    public DesensitizeSerializer(DesensitizeType type, Integer startIndex, Integer endIndex) {
        this.type = type;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (type) {
            // 手机号脱敏
            case MOBILE_PHONE:
                jsonGenerator.writeString(DesensitizedUtil.mobilePhone(String.valueOf(value)));
                break;
            // 车牌号脱敏
            case LICENSE_NUMBER:
                jsonGenerator.writeString(DesensitizedUtil.carLicense(String.valueOf(value)));
                break;
            // 身份证号脱敏
            case ID_CARD:
                jsonGenerator.writeString(DesensitizedUtil.idCardNum(String.valueOf(value), 3, 4));
                break;
            // 银行卡脱敏
            case BANK_CARD:
                jsonGenerator.writeString(DesensitizedUtil.bankCard(String.valueOf(value)));
                break;
            // 自定义脱敏
            case CUSTOM:
                jsonGenerator.writeString(CharSequenceUtil.hide(value, startIndex, endIndex));
                break;
            default:
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            // 判断数据类型是否为String类型
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 获取定义的注解
                Desensitize desensitize = beanProperty.getAnnotation(Desensitize.class);
                // 为null
                if (desensitize == null) {
                    desensitize = beanProperty.getContextAnnotation(Desensitize.class);
                }
                // 不为null
                if (desensitize != null) {
                    // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
                    return new DesensitizeSerializer(desensitize.type(), desensitize.startIndex(),
                            desensitize.endIndex());
                }
            }

            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }

        return serializerProvider.findNullValueSerializer(null);
    }
}