package io.appform.foxtrot.client.util;

import org.apache.commons.lang3.ClassUtils;

public class TypeChecker {

    public static boolean isPrimitive(Object object) {
        return ClassUtils.isPrimitiveOrWrapper(object.getClass());
    }
}
